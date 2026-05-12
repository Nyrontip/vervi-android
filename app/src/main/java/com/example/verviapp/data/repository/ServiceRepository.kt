package com.example.verviapp.data.repository

import com.example.verviapp.data.remote.VerviApi
import com.example.verviapp.data.remote.dto.ServiceDto
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceRepository @Inject constructor(
    private val api: VerviApi
) {
    suspend fun getServiceById(serviceId: Int): ApiResult<ServiceDto> =
        fetchOne { api.getServiceById(serviceId) }

    suspend fun getServicesByProvider(providerId: Int): ApiResult<List<ServiceDto>> =
        fetchList { api.getServicesByProvider(providerId) }

    private suspend fun fetchOne(
        apiCall: suspend () -> Response<ServiceDto>
    ): ApiResult<ServiceDto> = try {
        val response = apiCall()
        if (response.isSuccessful && response.body() != null) {
            ApiResult.Success(response.body()!!)
        } else {
            ApiResult.Error(
                response.errorBody()?.string() ?: "Servicio no encontrado",
                response.code()
            )
        }
    } catch (e: Exception) {
        ApiResult.Error(e.message ?: "Error de conexión")
    }

    private suspend fun fetchList(
        apiCall: suspend () -> Response<List<ServiceDto>>
    ): ApiResult<List<ServiceDto>> = try {
        val response = apiCall()
        if (response.isSuccessful) {
            ApiResult.Success(response.body() ?: emptyList())
        } else {
            ApiResult.Error(
                response.errorBody()?.string() ?: "Error al cargar servicios",
                response.code()
            )
        }
    } catch (e: Exception) {
        ApiResult.Error(e.message ?: "Error de conexión")
    }
}
