package com.example.verviapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.remote.dto.ServiceDto
import com.example.verviapp.data.repository.ApiResult
import com.example.verviapp.data.repository.ChatRepository
import com.example.verviapp.data.repository.ServiceRepository
import com.example.verviapp.data.session.SessionManager
import com.example.verviapp.viewmodel.state.ServiceDetailItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

data class ServiceDetailsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val detail: ServiceDetailItem? = null,
    val isOwner: Boolean = false,
    val counterpartUserId: Int? = null
)

@HiltViewModel
class ServiceDetailsViewModel @Inject constructor(
    private val repository: ServiceRepository,
    private val chatRepository: ChatRepository,
    private val sessionManager: SessionManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val serviceIdArg: Int = savedStateHandle.get<Int>("serviceId")
        ?: savedStateHandle.get<String>("serviceId")?.toIntOrNull()
        ?: 0

    private val _uiState = MutableStateFlow(ServiceDetailsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        if (serviceIdArg > 0) load(serviceIdArg)
    }

    fun load(serviceId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = repository.getServiceById(serviceId)) {
                is ApiResult.Success -> {
                    val dto = result.data
                    val currentUserId = sessionManager.getLoggedInUserId()
                    val isProvider = currentUserId == dto.providerUserId
                    val counterpart = if (isProvider) dto.client else dto.provider
                    val counterpartId = if (isProvider) dto.clientUserId else dto.providerUserId

                    _uiState.value = _uiState.value.copy(
                        detail = dto.toDetailItem(isProvider),
                        isOwner = isProvider,
                        counterpartUserId = counterpartId,
                        isLoading = false
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    suspend fun openChat(counterpartUserId: Int, requestId: Int?): Int? {
        return when (val result = chatRepository.findOrCreateConversation(counterpartUserId, requestId)) {
            is ApiResult.Success -> result.data.id
            is ApiResult.Error -> null
        }
    }

    fun retry() {
        if (serviceIdArg > 0) load(serviceIdArg)
    }

    private fun ServiceDto.toDetailItem(isProvider: Boolean): ServiceDetailItem {
        val counterpart = if (isProvider) client else provider
        val statusLabel = when (status) {
            "SCHEDULED" -> "Programado"
            "IN_PROGRESS" -> "En curso"
            "COMPLETED" -> "Completado"
            "CANCELLED" -> "Cancelado"
            else -> status
        }

        return ServiceDetailItem(
            serviceId = id,
            title = title,
            summary = summary ?: "",
            location = location ?: "",
            totalPriceText = "$${priceFormatter.format(totalPriceCop)} COP",
            dateText = dateFormatter.format(Date(parseDate(createdAt))),
            statusText = statusLabel,
            roleLabel = if (isProvider) "Como: Proveedor" else "Como: Cliente",
            counterpartName = counterpart?.name ?: "Usuario",
            counterpartRatingText = counterpart?.rating?.let { String.format(localeEsCo, "%.1f", it) } ?: "--",
            counterpartLocation = counterpart?.location ?: "",
            counterpartAvatarUrl = counterpart?.photoUrl?.takeIf(String::isNotBlank),
            evidenceImageUrls = evidence?.map { it.imageUrl } ?: emptyList(),
            chatSummaryText = "Ver conversación con ${counterpart?.name ?: "el usuario"}"
        )
    }

    private fun parseDate(iso: String?): Long {
        if (iso == null) return System.currentTimeMillis()
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            sdf.parse(iso)?.time ?: System.currentTimeMillis()
        } catch (_: Exception) {
            System.currentTimeMillis()
        }
    }

    private companion object {
        val localeEsCo: Locale = Locale.forLanguageTag("es-CO")
        val dateFormatter = SimpleDateFormat("dd 'de' MMMM, yyyy • hh:mm a", localeEsCo)
        val priceFormatter = NumberFormat.getNumberInstance(localeEsCo)
    }
}
