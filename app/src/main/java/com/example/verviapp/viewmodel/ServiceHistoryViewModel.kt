package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.remote.dto.ServiceDto
import com.example.verviapp.data.repository.ApiResult
import com.example.verviapp.data.repository.ServiceRepository
import com.example.verviapp.data.session.SessionManager
import com.example.verviapp.viewmodel.state.ServiceHistoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class ServiceHistoryUiState(
    val selectedTab: Int = 0,
    val services: List<ServiceHistoryItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class ServiceHistoryEvent {
    data class OpenDetails(val item: ServiceHistoryItem) : ServiceHistoryEvent()
}

@HiltViewModel
class ServiceHistoryViewModel @Inject constructor(
    private val repository: ServiceRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private var targetUserId: Int? = sessionManager.getLoggedInUserId()

    private val _uiState = MutableStateFlow(ServiceHistoryUiState())
    val uiState: StateFlow<ServiceHistoryUiState> = _uiState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<ServiceHistoryEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    fun loadForUser(userId: Int? = null) {
        val id = userId ?: sessionManager.getLoggedInUserId() ?: run {
            _uiState.value = _uiState.value.copy(isLoading = false, error = "Debes iniciar sesión", services = emptyList())
            return
        }
        targetUserId = id
        loadServices()
    }

    fun selectTab(index: Int) {
        val id = targetUserId ?: return
        if (_uiState.value.selectedTab == index && _uiState.value.services.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(selectedTab = index)
            return
        }
        _uiState.value = _uiState.value.copy(selectedTab = index)
        loadServices()
    }

    fun onServiceClick(item: ServiceHistoryItem) {
        viewModelScope.launch { _navigationEvents.emit(ServiceHistoryEvent.OpenDetails(item)) }
    }

    fun retry() {
        targetUserId?.let { loadServices() }
    }

    private fun loadServices() {
        val userId = targetUserId ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = repository.getServicesByProvider(userId)) {
                is ApiResult.Success -> {
                    val activeOnly = _uiState.value.selectedTab == 0
                    val filtered = result.data.filter { s ->
                        if (activeOnly) s.status in listOf("SCHEDULED", "IN_PROGRESS")
                        else s.status in listOf("COMPLETED", "CANCELLED")
                    }
                    _uiState.value = _uiState.value.copy(
                        services = filtered.map { it.toUiItem() },
                        isLoading = false, error = null
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = result.message)
                }
            }
        }
    }

    private fun ServiceDto.toUiItem(): ServiceHistoryItem {
        val formattedPrice = "\$${priceFormatter.format(totalPriceCop)} COP"
        val dateStr = dateFormatter.format(Date(isoParser.parse(createdAt ?: "")?.time ?: System.currentTimeMillis()))
        return ServiceHistoryItem(
            serviceId = id, title = title,
            provider = client?.name ?: "Cliente",
            date = dateStr, price = formattedPrice,
            imageUrl = imageUrl ?: "", status = status.toStatusLabel()
        )
    }

    private fun String.toStatusLabel(): String = when (this) {
        "SCHEDULED" -> "Programado"
        "IN_PROGRESS" -> "En curso"
        "COMPLETED" -> "Completado"
        "CANCELLED" -> "Cancelado"
        else -> this
    }

    private companion object {
        val localeEsCo = Locale.forLanguageTag("es-CO")
        val dateFormatter = SimpleDateFormat("dd MMM yyyy", localeEsCo)
        val priceFormatter = NumberFormat.getNumberInstance(localeEsCo)
        val isoParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
            timeZone = java.util.TimeZone.getTimeZone("UTC")
        }
    }
}
