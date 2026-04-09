package com.example.verviapp.viewmodel.state

data class NotificationItem(
    val id: Int,
    val title: String,
    val description: String,
    val time: String,
    val type: NotificationType,
    val unread: Boolean = false,
)

enum class NotificationType {
    APPLICATION,
    MESSAGE,
    CONFIRMED,
    PAYMENT,
    REMINDER
}
