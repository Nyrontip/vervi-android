package com.example.verviapp.model

data class NotificationItem(
    val id: String = java.util.UUID.randomUUID().toString(),
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
