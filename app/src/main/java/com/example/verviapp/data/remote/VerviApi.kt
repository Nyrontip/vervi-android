package com.example.verviapp.data.remote

import com.example.verviapp.data.remote.dto.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface VerviApi {

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<UserDto>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<UserDto>

    @GET("users/profile/me")
    suspend fun getCurrentProfile(): Response<UserDto>

    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body request: UserUpdateRequest
    ): Response<UserDto>

    @GET("categories")
    suspend fun getCategories(): Response<List<CategoryDto>>

    @GET("categories/{id}")
    suspend fun getCategoryById(@Path("id") id: Int): Response<CategoryDto>

    @GET("requests")
    suspend fun getRequests(): Response<List<RequestDto>>

    @GET("requests/{id}")
    suspend fun getRequestById(@Path("id") id: Int): Response<RequestDto>

    @GET("requests/client/{clientId}")
    suspend fun getRequestsByClient(@Path("clientId") clientId: Int): Response<List<RequestDto>>

    @POST("requests")
    suspend fun createRequest(@Body request: RequestCreateRequest): Response<RequestDto>

    @PUT("requests/{id}")
    suspend fun updateRequest(
        @Path("id") id: Int,
        @Body request: RequestDto
    ): Response<RequestDto>

    @DELETE("requests/{id}")
    suspend fun deleteRequest(@Path("id") id: Int): Response<Unit>

    // ── Notificaciones ──────────────────────────────────────────

    @GET("notifications/user/{userId}")
    suspend fun getUserNotifications(@Path("userId") userId: Int): Response<List<NotificationDto>>

    @GET("notifications/user/{userId}/unread")
    suspend fun getUnreadNotifications(@Path("userId") userId: Int): Response<List<NotificationDto>>

    @PUT("notifications/{id}/read")
    suspend fun markNotificationRead(@Path("id") id: Int): Response<Unit>

    @PUT("notifications/user/{userId}/read-all")
    suspend fun markAllNotificationsRead(@Path("userId") userId: Int): Response<Unit>

    // ── Servicios ──────────────────────────────────────────────

    @GET("services/{id}")
    suspend fun getServiceById(@Path("id") id: Int): Response<ServiceDto>

    @GET("services/provider/{providerId}")
    suspend fun getServicesByProvider(@Path("providerId") providerId: Int): Response<List<ServiceDto>>

    // ── Aplicaciones (postulaciones) ──────────────────────────

    @GET("applications/request/{requestId}")
    suspend fun getApplicationsByRequest(@Path("requestId") requestId: Int): Response<List<ApplicationDto>>

    @PUT("applications/{id}")
    suspend fun updateApplication(
        @Path("id") id: Int,
        @Body request: Map<String, String>
    ): Response<ApplicationDto>

    @POST("applications/{id}/accept")
    suspend fun acceptApplication(@Path("id") id: Int): Response<ServiceDto>

    // ── Chat ────────────────────────────────────────────────────

    @POST("chat/conversations/find-or-create")
    suspend fun findOrCreateConversation(@Body request: FindOrCreateConversationRequest): Response<ConversationDto>

    @GET("chat/messages/{conversationId}")
    suspend fun getMessages(@Path("conversationId") conversationId: Int): Response<List<MessageDto>>

    @GET("chat/conversations/{id}")
    suspend fun getConversation(@Path("id") id: Int): Response<ConversationDto>

    @POST("chat/messages")
    suspend fun sendMessage(@Body request: CreateMessageRequest): Response<MessageDto>

    // ── Upload ───────────────────────────────────────────────────

    @Multipart
    @POST("upload/image")
    suspend fun uploadImage(@Part file: MultipartBody.Part): Response<ImageUploadResponse>

    // ── Crear postulación ────────────────────────────────────────

    @POST("applications")
    suspend fun createApplication(@Body request: ApplicationCreateRequest): Response<ApplicationDto>

    // ── Reviews / Calificaciones ─────────────────────────────────

    @POST("reviews")
    suspend fun createReview(@Body request: ReviewCreateRequest): Response<ReviewDto>

    @GET("reviews/service/{serviceId}")
    suspend fun getReviewsByService(@Path("serviceId") serviceId: Int): Response<List<ReviewDto>>

    // ── Actualizar servicio (evidencia parcial) ───────────────────

    @PUT("services/{id}")
    suspend fun updateService(
        @Path("id") id: Int,
        @Body request: AddEvidenceBody
    ): Response<ServiceDto>
}