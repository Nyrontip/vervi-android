package com.example.verviapp.data.repository

import com.example.verviapp.data.remote.VerviApi
import com.example.verviapp.data.remote.dto.ReviewCreateRequest
import com.example.verviapp.data.remote.dto.ReviewDto
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewsRepository @Inject constructor(
    private val api: VerviApi
) {
    suspend fun createReview(
        serviceId: Int,
        reviewerUserId: Int,
        reviewedUserId: Int,
        rating: Int,
        comment: String? = null,
        evidenceImageUrl: String? = null
    ): ApiResult<ReviewDto> = fetchOne {
        api.createReview(
            ReviewCreateRequest(
                serviceId = serviceId,
                reviewerUserId = reviewerUserId,
                reviewedUserId = reviewedUserId,
                rating = rating,
                comment = comment?.takeIf { it.isNotBlank() },
                evidenceImageUrl = evidenceImageUrl
            )
        )
    }

    suspend fun getReviewsByService(serviceId: Int): ApiResult<List<ReviewDto>> = try {
        val response = api.getReviewsByService(serviceId)
        if (response.isSuccessful) {
            ApiResult.Success(response.body() ?: emptyList())
        } else {
            ApiResult.Error(
                response.errorBody()?.string() ?: "Error al cargar calificaciones",
                response.code()
            )
        }
    } catch (e: Exception) {
        ApiResult.Error(e.message ?: "Error de conexión")
    }

    private suspend fun fetchOne(
        apiCall: suspend () -> Response<ReviewDto>
    ): ApiResult<ReviewDto> = try {
        val response = apiCall()
        if (response.isSuccessful && response.body() != null) {
            ApiResult.Success(response.body()!!)
        } else {
            ApiResult.Error(
                response.errorBody()?.string() ?: "Error al enviar calificación",
                response.code()
            )
        }
    } catch (e: Exception) {
        ApiResult.Error(e.message ?: "Error de conexión")
    }
}
