package com.example.verviapp.data.repository

import com.example.verviapp.data.remote.VerviApi
import com.example.verviapp.data.remote.dto.*
import com.example.verviapp.data.session.SessionManager
import com.example.verviapp.viewmodel.state.ChatMessageState
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val api: VerviApi,
    private val sessionManager: SessionManager
) {
    private val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())

    suspend fun findOrCreateConversation(
        otherUserId: Int,
        requestId: Int?
    ): ApiResult<ConversationDto> {
        val currentUserId = sessionManager.getLoggedInUserId() ?: return ApiResult.Error("Debes iniciar sesión")
        val body = FindOrCreateConversationRequest(currentUserId, otherUserId, requestId)
        return fetchOne { api.findOrCreateConversation(body) }
    }

    suspend fun getMessages(conversationId: Int): ApiResult<List<ChatMessageState>> = fetchList {
        api.getMessages(conversationId)
    }

    suspend fun sendMessage(conversationId: Int, text: String): ApiResult<Unit> {
        val currentUserId = sessionManager.getLoggedInUserId() ?: return ApiResult.Error("Debes iniciar sesión")
        return executeAction { api.sendMessage(CreateMessageRequest(conversationId, currentUserId, text)) }
    }

    fun getLoggedInUserId(): Int? = sessionManager.getLoggedInUserId()

    fun toUiMessages(dtos: List<MessageDto>, currentUserId: Int): List<ChatMessageState> = dtos.map { dto ->
        ChatMessageState(
            text = dto.body,
            time = parseTime(dto.sentAt),
            isUser = dto.senderUserId == currentUserId,
            avatar = dto.sender?.photoUrl?.takeIf(String::isNotBlank)
        )
    }

    private fun parseTime(sentAt: String?): String {
        if (sentAt == null) return ""
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
                timeZone = java.util.TimeZone.getTimeZone("UTC")
            }
            timeFormatter.format(sdf.parse(sentAt) ?: Date())
        } catch (_: Exception) {
            sentAt.take(5)
        }
    }

    private suspend fun <T> fetchOne(apiCall: suspend () -> Response<T>): ApiResult<T> = try {
        val response = apiCall()
        if (response.isSuccessful && response.body() != null) {
            ApiResult.Success(response.body()!!)
        } else {
            ApiResult.Error(response.errorBody()?.string() ?: "Error", response.code())
        }
    } catch (e: Exception) {
        ApiResult.Error(e.message ?: "Error de conexión")
    }

    private suspend fun fetchList(apiCall: suspend () -> Response<List<MessageDto>>): ApiResult<List<ChatMessageState>> = try {
        val response = apiCall()
        if (response.isSuccessful) {
            val userId = getLoggedInUserId() ?: return ApiResult.Error("No session")
            val items = toUiMessages(response.body() ?: emptyList(), userId)
            ApiResult.Success(items)
        } else {
            ApiResult.Error(response.errorBody()?.string() ?: "Error", response.code())
        }
    } catch (e: Exception) {
        ApiResult.Error(e.message ?: "Error de conexión")
    }

    private suspend fun executeAction(apiCall: suspend () -> Response<*>): ApiResult<Unit> = try {
        val response = apiCall()
        if (response.isSuccessful) ApiResult.Success(Unit)
        else ApiResult.Error(response.errorBody()?.string() ?: "Error", response.code())
    } catch (e: Exception) {
        ApiResult.Error(e.message ?: "Error de conexión")
    }
}
