package com.example.verviapp.data.repository

import com.example.verviapp.data.remote.VerviApi
import com.example.verviapp.data.remote.dto.*
import com.example.verviapp.data.session.SessionManager
import com.example.verviapp.viewmodel.state.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val api: VerviApi,
    private val sessionManager: SessionManager
) {
    private val _userState = MutableStateFlow(User())
    val userState: StateFlow<User> = _userState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val currencyFormatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"))

    suspend fun getCurrentUser(): ApiResult<UserDto> {
        _isLoading.value = true
        _error.value = null

        return try {
            val response = api.getCurrentProfile()
            if (response.isSuccessful) {
                val body = response.body()!!
                _userState.value = body.toUiUser()
                _isLoading.value = false
                ApiResult.Success(body)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error al obtener perfil"
                _error.value = errorMsg
                _isLoading.value = false
                ApiResult.Error(errorMsg, response.code())
            }
        } catch (e: Exception) {
            val errorMsg = e.message ?: "Error de conexión"
            _error.value = errorMsg
            _isLoading.value = false
            ApiResult.Error(errorMsg)
        }
    }

    suspend fun getUserById(id: Int): ApiResult<UserDto> {
        _isLoading.value = true
        _error.value = null

        return try {
            val response = api.getUserById(id)
            if (response.isSuccessful) {
                val body = response.body()!!
                _isLoading.value = false
                ApiResult.Success(body)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error al obtener usuario"
                _error.value = errorMsg
                _isLoading.value = false
                ApiResult.Error(errorMsg, response.code())
            }
        } catch (e: Exception) {
            val errorMsg = e.message ?: "Error de conexión"
            _error.value = errorMsg
            _isLoading.value = false
            ApiResult.Error(errorMsg)
        }
    }

    suspend fun updateUser(
        id: Int,
        name: String? = null,
        bio: String? = null,
        location: String? = null,
        photoUrl: String? = null,
        suggestedPriceCop: Long? = null,
        isProvider: Boolean? = null,
        categoryIds: List<Int>? = null
    ): ApiResult<UserDto> {
        _isLoading.value = true
        _error.value = null

        return try {
            val request = UserUpdateRequest(
                name = name,
                bio = bio,
                location = location,
                photoUrl = photoUrl,
                suggestedPriceCop = suggestedPriceCop,
                isProvider = isProvider,
                categoryIds = categoryIds
            )
            val response = api.updateUser(id, request)
            if (response.isSuccessful) {
                val body = response.body()!!
                _userState.value = body.toUiUser()
                _isLoading.value = false
                ApiResult.Success(body)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error al actualizar perfil"
                _error.value = errorMsg
                _isLoading.value = false
                ApiResult.Error(errorMsg, response.code())
            }
        } catch (e: Exception) {
            val errorMsg = e.message ?: "Error de conexión"
            _error.value = errorMsg
            _isLoading.value = false
            ApiResult.Error(errorMsg)
        }
    }

    suspend fun getAllCategories(): ApiResult<List<CategoryDto>> {
        return try {
            val response = api.getCategories()
            if (response.isSuccessful) {
                ApiResult.Success(response.body()!!)
            } else {
                ApiResult.Error(response.errorBody()?.string() ?: "Error", response.code())
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error de conexión")
        }
    }

    fun getSessionUserId(): Int? = sessionManager.getLoggedInUserId()

    fun isLoggedIn(): Boolean = sessionManager.isLoggedIn()

    private fun UserDto.toUiUser(): User {
        val price = suggestedPriceCop?.let { "\$${currencyFormatter.format(it)}" } ?: "A convenir"
        val categoryNames = categories?.map { it.name } ?: emptyList()

        return User(
            id = id.toString(),
            name = name,
            email = email,
            bio = bio ?: "Sin biografía",
            location = location ?: "Sin ubicación",
            photoUrl = photoUrl ?: "",
            rating = rating,
            reviewCount = reviewCount,
            suggestedPrice = price,
            categories = if (categoryNames.isEmpty()) listOf("Sin categoría") else categoryNames,
            isProvider = isProvider,
            projectCount = projectCount,
            requestCount = requestCount
        )
    }
}