package com.example.verviapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.remote.dto.RequestDto
import com.example.verviapp.data.repository.ApiResult
import com.example.verviapp.data.repository.RequestRepository
import com.example.verviapp.viewmodel.state.ChatSummary
import com.example.verviapp.viewmodel.state.ClientSummary
import com.example.verviapp.viewmodel.state.RequestDetailItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

data class RequestDetailsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val request: RequestDetailItem? = null
)

@HiltViewModel
class RequestDetailsViewModel @Inject constructor(
    private val repository: RequestRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val requestId: Int = savedStateHandle.get<String>("requestId")
        ?.toIntOrNull()
        ?: 0

    private val _uiState = MutableStateFlow(RequestDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private val isoParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    private val currencyFormatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"))

    init {
        if (requestId > 0) load() else errorNoId()
    }

    fun retry() {
        if (requestId > 0) load() else errorNoId()
    }

    private fun errorNoId() {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            error = "ID de solicitud inválido"
        )
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = repository.getRequestDetail(requestId)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        request = result.data.toDetailItem(),
                        isLoading = false
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun RequestDto.toDetailItem(): RequestDetailItem {
        val dateStr = parseDate(createdAt)
        val images = mutableListOf<String>()
        imageUrl?.takeIf { it.isNotBlank() }?.let { images.add(it) }

        return RequestDetailItem(
            id = id.toString(),
            title = title,
            date = dateStr,
            price = formatBudget(budgetCop),
            location = location ?: "",
            description = description ?: "",
            status = status,
            roleLabel = "Como: Prestador",
            images = images,
            client = client?.let {
                ClientSummary(
                    name = it.name,
                    rating = it.rating.takeIf { r -> r > 0f },
                    location = it.location ?: "",
                    phone = null,
                    avatarUrl = it.photoUrl?.takeIf(String::isNotBlank)
                )
            }
        )
    }

    private fun parseDate(isoDate: String?): String {
        if (isoDate == null) return "--"
        return try {
            val date = isoParser.parse(isoDate) ?: return isoDate.take(10)
            val sdf = SimpleDateFormat("d MMM yyyy", Locale.forLanguageTag("es-CO"))
            sdf.format(date)
        } catch (_: Exception) {
            isoDate.take(10)
        }
    }

    private fun formatBudget(budgetCop: Long?): String {
        if (budgetCop == null) return "Sin presupuesto"
        return "$${currencyFormatter.format(budgetCop)} COP"
    }
}
