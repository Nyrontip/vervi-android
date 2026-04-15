package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.text.format.DateUtils
import com.example.verviapp.data.dao.NotificationDao
import com.example.verviapp.data.entity.NotificationEntity
import com.example.verviapp.data.repository.SampleData
import com.example.verviapp.viewmodel.state.NotificationItem
import com.example.verviapp.viewmodel.state.NotificationType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.Job
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
    private val notificationDao: NotificationDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()
    private var notificationsJob: Job? = null

    init {
        observeNotifications()
    }

    fun selectTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
        observeNotifications()
    }

    fun markAsRead(item: NotificationItem) {
        if (!item.unread) return

        viewModelScope.launch {
            try {
                notificationDao.markAsRead(item.id)
            } catch (throwable: Throwable) {
                _uiState.value = _uiState.value.copy(
                    error = throwable.message ?: "No fue posible marcar la notificacion"
                )
            }
        }
    }


    private fun observeNotifications() {
        notificationsJob?.cancel()
        notificationsJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val flow = if (_uiState.value.selectedTab == 0) {
                notificationDao.observeNotifications(SampleData.NOTIFICATIONS_USER_ID)
            } else {
                notificationDao.observeUnreadNotifications(SampleData.NOTIFICATIONS_USER_ID)
            }

            flow
                .map { entities -> entities.map { it.toUiItem() } }
                .catch { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = throwable.message ?: "Error al cargar notificaciones"
                    )
                }
                .collect { items ->
                    _uiState.value = _uiState.value.copy(
                        notifications = items,
                        isLoading = false,
                        error = null
                    )
                }
        }
    }

    private fun NotificationEntity.toUiItem(): NotificationItem {
        val relativeTime = DateUtils.getRelativeTimeSpanString(
            createdAt,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS
        ).toString()

        return NotificationItem(
            id = id,
            title = title,
            description = description,
            time = relativeTime,
            type = type.toNotificationType(),
            unread = isUnread
        )
    }

    private fun String.toNotificationType(): NotificationType {
        return NotificationType.entries.firstOrNull { it.name == this } ?: NotificationType.REMINDER
    }
}

