package com.example.verviapp.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)

@JsonClass(generateAdapter = true)
data class RegisterRequest(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String,
    @Json(name = "name") val name: String,
    @Json(name = "bio") val bio: String? = null,
    @Json(name = "location") val location: String? = null,
    @Json(name = "photoUrl") val photoUrl: String? = null,
    @Json(name = "isProvider") val isProvider: Boolean? = null,
    @Json(name = "categoryIds") val categoryIds: List<Int>? = null
)

@JsonClass(generateAdapter = true)
data class AuthResponse(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "user") val user: UserDto
)

@JsonClass(generateAdapter = true)
data class UserDto(
    @Json(name = "id") val id: Int,
    @Json(name = "email") val email: String,
    @Json(name = "name") val name: String,
    @Json(name = "bio") val bio: String?,
    @Json(name = "location") val location: String?,
    @Json(name = "photoUrl") val photoUrl: String?,
    @Json(name = "rating") val rating: Float,
    @Json(name = "reviewCount") val reviewCount: Int,
    @Json(name = "suggestedPriceCop") val suggestedPriceCop: Long?,
    @Json(name = "isProvider") val isProvider: Boolean,
    @Json(name = "isOnline") val isOnline: Boolean,
    @Json(name = "projectCount") val projectCount: Int,
    @Json(name = "requestCount") val requestCount: Int,
    @Json(name = "categories") val categories: List<CategoryDto>? = null,
    @Json(name = "createdAt") val createdAt: String?,
    @Json(name = "updatedAt") val updatedAt: String?
)

@JsonClass(generateAdapter = true)
data class UserUpdateRequest(
    @Json(name = "name") val name: String? = null,
    @Json(name = "bio") val bio: String? = null,
    @Json(name = "location") val location: String? = null,
    @Json(name = "photoUrl") val photoUrl: String? = null,
    @Json(name = "suggestedPriceCop") val suggestedPriceCop: Long? = null,
    @Json(name = "isProvider") val isProvider: Boolean? = null,
    @Json(name = "categoryIds") val categoryIds: List<Int>? = null
)

@JsonClass(generateAdapter = true)
data class CategoryDto(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String
)

@JsonClass(generateAdapter = true)
data class RequestDto(
    @Json(name = "id") val id: Int,
    @Json(name = "clientUserId") val clientUserId: Int?,
    @Json(name = "categoryId") val categoryId: Int?,
    @Json(name = "status") val status: String,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String?,
    @Json(name = "location") val location: String?,
    @Json(name = "budgetCop") val budgetCop: Long?,
    @Json(name = "requiredDateMillis") val requiredDateMillis: Long?,
    @Json(name = "imageUrl") val imageUrl: String?,
    @Json(name = "isUrgent") val isUrgent: Boolean,
    @Json(name = "isActive") val isActive: Boolean,
    @Json(name = "applicationCount") val applicationCount: Int,
    @Json(name = "category") val category: CategoryDto?,
    @Json(name = "client") val client: UserDto?,
    @Json(name = "createdAt") val createdAt: String?,
    @Json(name = "updatedAt") val updatedAt: String?,
    @Json(name = "services") val services: List<ServiceSummaryDto>? = null
)

data class ServiceSummaryDto(
    val id: Int,
    val providerUserId: Int,
    val provider: UserDto?,
    val title: String,
    val createdAt: String?
)

@JsonClass(generateAdapter = true)
data class RequestCreateRequest(
    @Json(name = "clientUserId") val clientUserId: Int,
    @Json(name = "categoryId") val categoryId: Int? = null,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String? = null,
    @Json(name = "location") val location: String? = null,
    @Json(name = "budgetCop") val budgetCop: Long? = null,
    @Json(name = "requiredDateMillis") val requiredDateMillis: Long? = null,
    @Json(name = "imageUrl") val imageUrl: String? = null,
    @Json(name = "isUrgent") val isUrgent: Boolean = false,
    @Json(name = "isActive") val isActive: Boolean = true,
    @Json(name = "status") val status: String = "Pendiente"
)

@JsonClass(generateAdapter = true)
data class NotificationDto(
    @Json(name = "id") val id: Int,
    @Json(name = "userId") val userId: Int,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String,
    @Json(name = "type") val type: String,
    @Json(name = "isUnread") val isUnread: Boolean,
    @Json(name = "requestId") val requestId: Int? = null,
    @Json(name = "createdAt") val createdAt: String
)

data class ConversationDto(
    val id: Int,
    val participantAUserId: Int,
    val participantBUserId: Int,
    val requestId: Int? = null,
    val participantA: UserDto? = null,
    val participantB: UserDto? = null
)

data class EvidenceDto(
    val imageUrl: String,
    val caption: String? = null
)

data class ServiceDto(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "summary") val summary: String?,
    @Json(name = "status") val status: String,
    @Json(name = "totalPriceCop") val totalPriceCop: Long,
    @Json(name = "location") val location: String?,
    @Json(name = "clientUserId") val clientUserId: Int,
    @Json(name = "providerUserId") val providerUserId: Int,
    @Json(name = "client") val client: UserDto?,
    @Json(name = "provider") val provider: UserDto?,
    @Json(name = "requestId") val requestId: Int?,
    @Json(name = "createdAt") val createdAt: String?,
    @Json(name = "completedAt") val completedAt: String?,
    @Json(name = "evidence") val evidence: List<EvidenceDto>? = null,
    @Json(name = "imageUrl") val imageUrl: String? = null
)

data class FindOrCreateConversationRequest(
    val participantAUserId: Int,
    val participantBUserId: Int,
    val requestId: Int? = null
)

data class MessageDto(
    val id: Int,
    val conversationId: Int,
    val senderUserId: Int,
    val body: String,
    val isRead: Boolean,
    val sentAt: String?,
    val sender: UserDto? = null
)

data class CreateMessageRequest(
    val conversationId: Int,
    val senderUserId: Int,
    val body: String
)

data class ApplicationDto(
    val id: Int,
    val requestId: Int,
    val providerUserId: Int,
    val provider: UserDto? = null,
    val presentationMessage: String? = null,
    val proposedPriceCop: Long? = null,
    val immediateAvailability: Boolean = false,
    val status: String = "PENDING",
    val createdAt: String? = null
)

@JsonClass(generateAdapter = true)
data class ApiError(
    @Json(name = "message") val message: String,
    @Json(name = "statusCode") val statusCode: Int? = null
)

@JsonClass(generateAdapter = true)
data class ImageUploadResponse(
    @Json(name = "secure_url") val secureUrl: String
)