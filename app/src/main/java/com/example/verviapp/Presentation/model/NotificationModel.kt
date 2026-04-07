package com.example.verviapp.Presentation.model

import java.util.UUID

data class NotificationItem(
    val id: String = UUID.randomUUID().toString(),
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
