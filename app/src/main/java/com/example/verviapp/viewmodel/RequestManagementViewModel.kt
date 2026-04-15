package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.ui.graphics.Color
import com.example.verviapp.viewmodel.state.RequestItem
import com.example.verviapp.data.dao.RequestDao
import com.example.verviapp.data.entity.RequestEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RequestUiState(
    val selectedTab: Int = 0,
    val requests: List<RequestItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class RequestEvent {
    data class OpenRequest(val item: RequestItem) : RequestEvent()
    object CreateNew : RequestEvent()
}

@HiltViewModel
class RequestManagementViewModel @Inject constructor(
    private val requestDao: RequestDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(RequestUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<RequestEvent>()
    val events = _events.asSharedFlow()

    init {
        loadRequests()
    }

    fun selectTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
        val activeOnly = when (index) {
            0 -> true
            1 -> false
            else -> null
        }
        loadRequests(activeOnly)
    }

    fun onOpen(item: RequestItem) {
        viewModelScope.launch { _events.emit(RequestEvent.OpenRequest(item)) }
    }

    fun onCreateNew() {
        viewModelScope.launch { _events.emit(RequestEvent.CreateNew) }
    }

    fun refresh() { loadRequests() }

    private fun loadRequests(activeOnly: Boolean? = null) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                val flow = when (activeOnly) {
                    true -> requestDao.getRequestsByStatus(true)
                    false -> requestDao.getRequestsByStatus(false)
                    null -> requestDao.getAllRequests()
                }

                flow.collect { entities ->
                    val items = entities.map { entity -> mapEntityToRequestItem(entity) }
                    _uiState.value = _uiState.value.copy(requests = items, isLoading = false)
                }
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(error = t.message ?: "Error desconocido", isLoading = false)
            }
        }
    }

    /**
     * Mapea RequestEntity de Room a RequestItem para UI.
     * Usa los datos disponibles en la entidad; los colores y íconos se calculan según el status.
     */
    private fun mapEntityToRequestItem(entity: RequestEntity): RequestItem {
        val (statusColor, buttonText, secondaryIcon) = when (entity.status) {
            "En curso" -> Triple(Color(0xFF10B981), "Gestionar", Icons.Default.MoreHoriz)
            "Pendiente" -> Triple(Color(0xFFF59E0B), "Gestionar", Icons.Default.MoreHoriz)
            "Borrador" -> Triple(Color.Gray, "Continuar", Icons.Default.DeleteOutline)
            else -> Triple(Color.Gray, "Ver", Icons.Default.MoreHoriz)
        }

        return RequestItem(
            id = entity.id,
            status = entity.status,
            statusColor = statusColor,
            title = entity.title,
            date = entity.date,
            applications = entity.applications,
            imageUrl = entity.imageUrl,
            buttonText = buttonText,
            secondaryIcon = secondaryIcon
        )
    }
}

