package com.example.verviapp.data.repository

import com.example.verviapp.data.remote.VerviApi
import com.example.verviapp.data.remote.dto.*
import com.example.verviapp.data.session.SessionManager
import com.example.verviapp.viewmodel.state.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val code: Int? = null) : ApiResult<Nothing>()
}

@Singleton
class AuthRepository @Inject constructor(
    private val api: VerviApi,
    private val sessionManager: SessionManager
) {
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    suspend fun login(email: String, password: String): ApiResult<AuthResponse> {
        _authState.value = _authState.value.copy(isLoading = true, errorMessage = null)

        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val body = response.body()!!
                sessionManager.saveUserSession(body.user.id, body.accessToken)
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    errorMessage = null
                )
                ApiResult.Success(body)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error de autenticación"
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    errorMessage = errorMsg
                )
                ApiResult.Error(errorMsg, response.code())
            }
        } catch (e: Exception) {
            val errorMsg = e.message ?: "Error de conexión"
            _authState.value = _authState.value.copy(
                isLoading = false,
                errorMessage = errorMsg
            )
            ApiResult.Error(errorMsg)
        }
    }

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): ApiResult<UserDto> {
        _authState.value = _authState.value.copy(isLoading = true, errorMessage = null)

        return try {
            val request = RegisterRequest(
                email = email,
                password = password,
                name = name
            )
            val response = api.register(request)
            if (response.isSuccessful) {
                val body = response.body()!!
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    errorMessage = null
                )
                ApiResult.Success(body)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error de registro"
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    errorMessage = errorMsg
                )
                ApiResult.Error(errorMsg, response.code())
            }
        } catch (e: Exception) {
            val errorMsg = e.message ?: "Error de conexión"
            _authState.value = _authState.value.copy(
                isLoading = false,
                errorMessage = errorMsg
            )
            ApiResult.Error(errorMsg)
        }
    }

    fun logout() {
        sessionManager.clearSession()
    }

    fun hasActiveSession(): Boolean = sessionManager.isLoggedIn()

    fun clearError() {
        _authState.value = _authState.value.copy(errorMessage = null)
    }
}