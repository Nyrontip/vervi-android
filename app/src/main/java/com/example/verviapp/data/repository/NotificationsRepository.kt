package com.example.verviapp.data.repository

import com.example.verviapp.data.remote.VerviApi
import com.example.verviapp.data.remote.dto.NotificationDto
import com.example.verviapp.data.session.SessionManager
import com.example.verviapp.viewmodel.state.NotificationItem
import com.example.verviapp.viewmodel.state.NotificationType
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationsRepository @Inject constructor(
    private val api: VerviApi,
    private val sessionManager: SessionManager
) {
    private val isoParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    suspend fun getAll(userId: Int): ApiResult<List<NotificationItem>> =
        fetchList { api.getUserNotifications(userId) }

    suspend fun getUnread(userId: Int): ApiResult<List<NotificationItem>> =
        fetchList { api.getUnreadNotifications(userId) }

    suspend fun markAsRead(notificationId: Int): ApiResult<Unit> =
        executeAction { api.markNotificationRead(notificationId) }

    suspend fun markAllAsRead(userId: Int): ApiResult<Unit> =
        executeAction { api.markAllNotificationsRead(userId) }

    fun getLoggedInUserId(): Int? = sessionManager.getLoggedInUserId()

    // ── Helpers ───────────────────────────────────────────────

    private suspend fun fetchList(
        apiCall: suspend () -> Response<List<NotificationDto>>
    ): ApiResult<List<NotificationItem>> = try {
        val response = apiCall()
        if (response.isSuccessful) {
            ApiResult.Success((response.body() ?: emptyList()).map { it.toUiItem() })
        } else {
            ApiResult.Error(
                response.errorBody()?.string() ?: "Error al cargar notificaciones",
                response.code()
            )
        }
    } catch (e: Exception) {
        ApiResult.Error(e.message ?: "Error de conexión")
    }

    private suspend fun executeAction(
        apiCall: suspend () -> Response<Unit>
    ): ApiResult<Unit> = try {
        val response = apiCall()
        if (response.isSuccessful) {
            ApiResult.Success(Unit)
        } else {
            ApiResult.Error(
                response.errorBody()?.string() ?: "Error al realizar la operación",
                response.code()
            )
        }
    } catch (e: Exception) {
        ApiResult.Error(e.message ?: "Error de conexión")
    }

    private fun NotificationDto.toUiItem(): NotificationItem {
        val relativeTime = parseRelativeTime(createdAt)

        return NotificationItem(
            id = id,
            title = title,
            description = description,
            time = relativeTime,
            type = type.toNotificationType(),
            unread = isUnread
        )
    }

    private fun parseRelativeTime(isoDate: String): String {
        return try {
            val date = isoParser.parse(isoDate) ?: return isoDate
            val now = System.currentTimeMillis()
            val diff = now - date.time

            val minutes = diff / 60_000
            val hours = minutes / 60
            val days = hours / 24

            when {
                minutes < 1 -> "Ahora"
                minutes < 60 -> "Hace $minutes min"
                hours < 24 -> "Hace $hours h"
                days < 7 -> "Hace $days d"
                days < 30 -> "Hace ${days / 7} sem"
                else -> {
                    val sdf = SimpleDateFormat("d MMM", Locale.forLanguageTag("es-CO"))
                    sdf.format(date)
                }
            }
        } catch (_: Exception) {
            isoDate
        }
    }

    private fun String.toNotificationType(): NotificationType {
        return NotificationType.entries.firstOrNull { it.name == this } ?: NotificationType.REMINDER
    }
}
