package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.repository.ApiResult
import com.example.verviapp.data.repository.NotificationsRepository
import com.example.verviapp.viewmodel.state.NotificationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationsUiState(
    val selectedTab: Int = 0,
    val notifications: List<NotificationItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val repository: NotificationsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    fun selectTab(index: Int) {
        if (_uiState.value.selectedTab == index) return
        _uiState.value = _uiState.value.copy(selectedTab = index)
        loadNotifications()
    }

    fun markAsRead(item: NotificationItem) {
        if (!item.unread) return

        viewModelScope.launch {
            when (repository.markAsRead(item.id)) {
                is ApiResult.Success -> {
                    // Reflejar el cambio localmente sin recargar todo
                    val updated = _uiState.value.notifications.map {
                        if (it.id == item.id) it.copy(unread = false) else it
                    }
                    _uiState.value = _uiState.value.copy(notifications = updated)
                }
                is ApiResult.Error -> { /* silencioso: no bloquear al usuario */ }
            }
        }
    }

    fun retry() {
        loadNotifications()
    }

    private fun loadNotifications() {
        val userId = repository.getLoggedInUserId()
        if (userId == null) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = "Debes iniciar sesión para ver tus notificaciones"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = if (_uiState.value.selectedTab == 0) {
                repository.getAll(userId)
            } else {
                repository.getUnread(userId)
            }

            when (result) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        notifications = result.data,
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
}
