package com.example.verviapp.data.repository

import com.example.verviapp.data.remote.VerviApi
import com.example.verviapp.data.remote.dto.*
import com.example.verviapp.data.session.SessionManager
import com.example.verviapp.viewmodel.state.ChatMessageState
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val api: VerviApi,
    private val sessionManager: SessionManager
) {
    private val bogotaFormatter = SimpleDateFormat("hh:mm a", Locale.forLanguageTag("es-CO")).apply {
        timeZone = TimeZone.getTimeZone("America/Bogota")
    }

    suspend fun findOrCreateConversation(
        otherUserId: Int,
        requestId: Int?
    ): ApiResult<ConversationDto> {
        val currentUserId = sessionManager.getLoggedInUserId() ?: return ApiResult.Error("Debes iniciar sesión")
        val body = FindOrCreateConversationRequest(currentUserId, otherUserId, requestId)
        return fetchOne { api.findOrCreateConversation(body) }
    }

    suspend fun getConversation(conversationId: Int): ApiResult<ConversationDto> =
        fetchOne { api.getConversation(conversationId) }

    suspend fun getMessages(conversationId: Int): ApiResult<List<ChatMessageState>> = fetchList {
        api.getMessages(conversationId)
    }

    suspend fun sendMessage(conversationId: Int, text: String): ApiResult<Unit> {
        val currentUserId = sessionManager.getLoggedInUserId() ?: return ApiResult.Error("Debes iniciar sesión")
        return executeAction { api.sendMessage(CreateMessageRequest(conversationId, currentUserId, text)) }
    }

    fun getLoggedInUserId(): Int? = sessionManager.getLoggedInUserId()

    fun otherParticipant(conv: ConversationDto): UserDto? {
        val currentUserId = getLoggedInUserId() ?: return null
        return if (conv.participantAUserId == currentUserId) conv.participantB else conv.participantA
    }

    fun toUiMessages(dtos: List<MessageDto>, currentUserId: Int): List<ChatMessageState> = dtos.map { dto ->
        ChatMessageState(
            id = dto.id,
            text = dto.body,
            time = parseBogotaTime(dto.sentAt),
            isUser = dto.senderUserId == currentUserId,
            avatar = dto.sender?.photoUrl?.takeIf(String::isNotBlank)
        )
    }

    private fun parseBogotaTime(sentAt: String?): String {
        if (sentAt == null) return ""
        return try {
            val utcParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            val utcDate = utcParser.parse(sentAt) ?: Date()
            // Convertir UTC a COT (UTC-5: Colombia)
            val cotMillis = utcDate.time - (5 * 60 * 60 * 1000L)
            bogotaFormatter.format(Date(cotMillis))
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
