package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.model.NotificationItem
import com.example.verviapp.repository.NotificationsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class NotificationsUiState(
    val selectedTab: Int = 0,
    val notifications: List<NotificationItem> = emptyList()
)

class NotificationsViewModel(
    private val repository: NotificationsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            val all = repository.getAll()
            _uiState.value = _uiState.value.copy(notifications = all)
        }
    }

    fun selectTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
    }

    fun markAsRead(item: NotificationItem) {
        viewModelScope.launch {
            val updated = item.copy(unread = false)
            repository.update(updated)
            loadNotifications()
        }
    }

    fun refresh() {
        loadNotifications()
    }
}

