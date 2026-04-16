package com.example.verviapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.dao.ServiceDao
import com.example.verviapp.data.repository.SampleData
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
import javax.inject.Inject

data class ServiceDetailsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val detail: ServiceDetailItem? = null
)

@HiltViewModel
class ServiceDetailsViewModel @Inject constructor(
    private val serviceDao: ServiceDao,
    private val sessionManager: SessionManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val serviceIdArg: Int = savedStateHandle.get<String>("serviceId")
        ?.toIntOrNull()
        ?: 0

    private val _uiState = MutableStateFlow(ServiceDetailsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        if (serviceIdArg > 0) {
            load(serviceIdArg)
        }
    }

    fun load(serviceId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val viewerUserId = sessionManager.getLoggedInUserId() ?: SampleData.SERVICE_LOCAL_USER_ID
                val row = serviceDao.getServiceDetailsRow(serviceId, viewerUserId)
                if (row == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "No se encontro el servicio $serviceId"
                    )
                    return@launch
                }

                val evidenceUrls = serviceDao.getServiceEvidenceUrls(serviceId)
                val item = ServiceDetailItem(
                    serviceId = row.serviceId,
                    title = row.title,
                    summary = row.summary.ifBlank { "Sin resumen disponible." },
                    location = row.location,
                    totalPriceText = "$${priceFormatter.format(row.totalPriceCop)}",
                    dateText = dateFormatter.format(Date(row.dateMillis)),
                    statusText = if (row.status.equals("COMPLETED", ignoreCase = true)) "Completado" else row.status,
                    roleLabel = row.roleLabel,
                    counterpartName = row.counterpartName,
                    counterpartRatingText = String.format(localeEsCo, "%.1f", row.counterpartRating),
                    counterpartLocation = row.counterpartLocation,
                    counterpartAvatarUrl = row.counterpartAvatarUrl
                        ?.takeUnless { it.isBlank() }
                        ?: DEFAULT_AVATAR_URL,
                    evidenceImageUrls = evidenceUrls,
                    chatSummaryText = row.chatPreview ?: "Ver conversacion con el usuario"
                )

                _uiState.value = _uiState.value.copy(isLoading = false, detail = item)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = t.message ?: "Error desconocido"
                )
            }
        }
    }

    private companion object {
        val localeEsCo: Locale = Locale.forLanguageTag("es-CO")
        val dateFormatter = SimpleDateFormat("dd 'de' MMMM, yyyy • hh:mm a", localeEsCo)
        val priceFormatter = NumberFormat.getNumberInstance(localeEsCo)
        const val DEFAULT_AVATAR_URL = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&h=200&fit=crop"
    }
}


