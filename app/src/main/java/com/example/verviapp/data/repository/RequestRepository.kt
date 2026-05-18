package com.example.verviapp.data.repository

import com.example.verviapp.data.remote.VerviApi
import com.example.verviapp.data.remote.dto.*
import retrofit2.Response
import com.example.verviapp.data.session.SessionManager
import com.example.verviapp.viewmodel.state.HomeState
import com.example.verviapp.viewmodel.state.Service
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestRepository @Inject constructor(
    private val api: VerviApi,
    private val sessionManager: SessionManager
) {
    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    private val currencyFormatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"))

    suspend fun loadHomeRequests(
        searchQuery: String = "",
        category: String = "Todos"
    ): ApiResult<List<Service>> {
        _homeState.value = _homeState.value.copy(isLoading = true, errorMessage = null)

        return try {
            val response = api.getRequests()
            if (response.isSuccessful) {
                val allRequests = response.body() ?: emptyList()

                val filtered = allRequests.filter { request ->
                    val matchesSearch = searchQuery.isBlank() ||
                            request.title.lowercase().contains(searchQuery.lowercase()) ||
                            request.description?.lowercase()?.contains(searchQuery.lowercase()) == true ||
                            request.location?.lowercase()?.contains(searchQuery.lowercase()) == true

                    val matchesCategory = category == "Todos" ||
                            request.category?.name == category

                    matchesSearch && matchesCategory && request.isActive
                }

                val services = filtered.map { it.toUiService() }
                _homeState.value = _homeState.value.copy(
                    isLoading = false,
                    errorMessage = null,
                    services = services
                )
                ApiResult.Success(services)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error al cargar solicitudes"
                _homeState.value = _homeState.value.copy(
                    isLoading = false,
                    errorMessage = errorMsg
                )
                ApiResult.Error(errorMsg, response.code())
            }
        } catch (e: Exception) {
            val errorMsg = e.message ?: "Error de conexión"
            _homeState.value = _homeState.value.copy(
                isLoading = false,
                errorMessage = errorMsg
            )
            ApiResult.Error(errorMsg)
        }
    }

    suspend fun loadCategories(): ApiResult<List<CategoryDto>> {
        return try {
            val response = api.getCategories()
            if (response.isSuccessful) {
                val cats = response.body() ?: emptyList()
                _categories.value = listOf("Todos") + cats.map { it.name }
                ApiResult.Success(cats)
            } else {
                ApiResult.Error(response.errorBody()?.string() ?: "Error", response.code())
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error de conexión")
        }
    }

    // ── Gestión de solicitudes del usuario ──────────────────────

    suspend fun getUserRequests(clientId: Int, activeOnly: Boolean?): ApiResult<List<RequestDto>> =
        fetchList { api.getRequestsByClient(clientId) }

    suspend fun getRequestDetail(requestId: Int): ApiResult<RequestDto> =
        fetchOne { api.getRequestById(requestId) }

    suspend fun createRequest(data: RequestCreateRequest): ApiResult<RequestDto> =
        fetchOne { api.createRequest(data) }

    suspend fun updateRequestStatus(requestId: Int, status: String): ApiResult<RequestDto> {
        // Fetch current data then send only the status update
        val current = when (val r = getRequestDetail(requestId)) {
            is ApiResult.Success -> r.data
            is ApiResult.Error -> return ApiResult.Error(r.message, r.code)
        }
        val mappedStatus = when (status) {
            "CANCELLED", "Cancelada" -> "Cerrado"
            "COMPLETED", "Completado" -> "Cerrado"
            else -> status
        }
        return fetchOne {
            api.updateRequest(
                requestId,
                current.copy(
                    status = mappedStatus,
                    isActive = mappedStatus !in listOf("CANCELLED", "COMPLETED", "Cancelada", "Completado", "Cerrado")
                )
            )
        }
    }

    suspend fun deleteRequest(requestId: Int): ApiResult<Unit> =
        executeAppAction { api.deleteRequest(requestId) }

    // ── Postulaciones ───────────────────────────────────────────

    suspend fun getApplicationsByRequest(requestId: Int): ApiResult<List<ApplicationDto>> =
        fetchAppList { api.getApplicationsByRequest(requestId) }

    suspend fun acceptApplication(applicationId: Int): ApiResult<Unit> =
        executeAppAction { api.acceptApplication(applicationId) }

    suspend fun rejectApplication(applicationId: Int): ApiResult<Unit> =
        executeAppAction { api.updateApplication(applicationId, mapOf("status" to "REJECTED")) }

    suspend fun createApplication(data: ApplicationCreateRequest): ApiResult<ApplicationDto> =
        fetchAppOne { api.createApplication(data) }

    fun getLoggedInUserId(): Int? = sessionManager.getLoggedInUserId()


    // ── Helpers ────────────────────────────────────────────────

    private suspend fun fetchOne(
        apiCall: suspend () -> Response<RequestDto>
    ): ApiResult<RequestDto> = try {
        val response = apiCall()
        if (response.isSuccessful && response.body() != null) {
            ApiResult.Success(response.body()!!)
        } else {
            ApiResult.Error(
                response.errorBody()?.string() ?: "Solicitud no encontrada",
                response.code()
            )
        }
    } catch (e: Exception) {
        ApiResult.Error(e.message ?: "Error de conexión")
    }

    private suspend fun fetchAppList(
        apiCall: suspend () -> Response<List<ApplicationDto>>
    ): ApiResult<List<ApplicationDto>> = try {
        val response = apiCall()
        if (response.isSuccessful) {
            ApiResult.Success(response.body() ?: emptyList())
        } else {
            ApiResult.Error(
                response.errorBody()?.string() ?: "Error al cargar postulaciones",
                response.code()
            )
        }
    } catch (e: Exception) {
        ApiResult.Error(e.message ?: "Error de conexión")
    }

    private suspend fun executeAppAction(
        apiCall: suspend () -> Response<*>
    ): ApiResult<Unit> = try {
        val response = apiCall()
        if (response.isSuccessful) ApiResult.Success(Unit)
        else ApiResult.Error(response.errorBody()?.string() ?: "Error", response.code())
    } catch (e: Exception) {
        ApiResult.Error(e.message ?: "Error de conexión")
    }

    private suspend fun fetchAppOne(
        apiCall: suspend () -> Response<ApplicationDto>
    ): ApiResult<ApplicationDto> = try {
        val response = apiCall()
        if (response.isSuccessful && response.body() != null) {
            ApiResult.Success(response.body()!!)
        } else {
            ApiResult.Error(
                response.errorBody()?.string() ?: "Error al enviar postulación",
                response.code()
            )
        }
    } catch (e: Exception) {
        ApiResult.Error(e.message ?: "Error de conexión")
    }


    private suspend fun fetchList(
        apiCall: suspend () -> Response<List<RequestDto>>
    ): ApiResult<List<RequestDto>> = try {
        val response = apiCall()
        if (response.isSuccessful) {
            ApiResult.Success(response.body() ?: emptyList())
        } else {
            ApiResult.Error(
                response.errorBody()?.string() ?: "Error al cargar solicitudes",
                response.code()
            )
        }
    } catch (e: Exception) {
        ApiResult.Error(e.message ?: "Error de conexión")
    }

    fun onSearchChange(query: String) {
        _homeState.value = _homeState.value.copy(searchQuery = query)
    }

    fun onCategoryChange(category: String) {
        _homeState.value = _homeState.value.copy(selectedCategory = category)
    }

    private fun RequestDto.toUiService(): Service {
        val formattedPrice = budgetCop?.let { "\$${currencyFormatter.format(it)} COP" } ?: "A convenir"

        return Service(
            id = id,
            title = title,
            location = location ?: "",
            price = formattedPrice,
            applicationCount = applicationCount,
            isUrgent = isUrgent,
            imageUrl = imageUrl ?: "",
            category = category?.name ?: "Sin categoría"
        )
    }
}
