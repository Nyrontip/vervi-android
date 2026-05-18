package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.ui.graphics.Color
import com.example.verviapp.data.remote.dto.RequestDto
import com.example.verviapp.data.repository.ApiResult
import com.example.verviapp.data.repository.RequestRepository
import com.example.verviapp.viewmodel.state.RequestItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val repository: RequestRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RequestUiState())
    val uiState: StateFlow<RequestUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<RequestEvent>()
    val events = _events.asSharedFlow()

    init {
        loadRequests()
    }

    fun selectTab(index: Int) {
        if (_uiState.value.selectedTab == index) return
        _uiState.value = _uiState.value.copy(selectedTab = index)
        loadRequests()
    }

    fun onOpen(item: RequestItem) {
        viewModelScope.launch { _events.emit(RequestEvent.OpenRequest(item)) }
    }

    fun onCreateNew() {
        viewModelScope.launch { _events.emit(RequestEvent.CreateNew) }
    }

    fun retry() {
        loadRequests()
    }

    private fun loadRequests() {
        val userId = repository.getLoggedInUserId()
        if (userId == null) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = "Debes iniciar sesión"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = repository.getUserRequests(userId, null)) {
                is ApiResult.Success -> {
                    val allRequests = result.data
                    val filtered = when (_uiState.value.selectedTab) {
                        0 -> allRequests.filter { it.isActive && it.status != "Borrador" }
                        1 -> allRequests.filter { !it.isActive && it.status != "Borrador" }
                        2 -> allRequests.filter { it.status == "Borrador" }
                        else -> allRequests
                    }

                    _uiState.value = _uiState.value.copy(
                        requests = filtered.map { it.toUiItem() },
                        isLoading = false,
                        error = null
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


    private fun RequestDto.toUiItem(): RequestItem {
        val dateStr = createdAt?.take(10) ?: ""

        val (statusColor, buttonText, secondaryIcon) = when (status) {
            "En curso" -> Triple(Color(0xFF10B981), "Gestionar", Icons.Default.MoreHoriz)
            "Pendiente" -> Triple(Color(0xFFF59E0B), "Gestionar", Icons.Default.MoreHoriz)
            "Borrador" -> Triple(Color.Gray, "Continuar", Icons.Default.DeleteOutline)
            else -> Triple(Color.Gray, "Ver", Icons.Default.MoreHoriz)
        }

        return RequestItem(
            id = id,
            status = status,
            statusColor = statusColor,
            title = title,
            date = dateStr,
            applications = "$applicationCount Postulaciones",
            imageUrl = imageUrl ?: "",
            buttonText = buttonText,
            secondaryIcon = secondaryIcon
        )
    }
}
