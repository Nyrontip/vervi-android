package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.repository.ApiResult
import com.example.verviapp.data.repository.AuthRepository
import com.example.verviapp.data.session.SessionManager
import com.example.verviapp.viewmodel.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthEvent {
    object LoginSuccess : AuthEvent()
    object RegisterSuccess : AuthEvent()
    data class Error(val message: String) : AuthEvent()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<AuthEvent>()
    val events = _events.asSharedFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val normalizedEmail = email.trim().lowercase()
            if (normalizedEmail.isBlank() || password.isBlank()) {
                _state.value = _state.value.copy(errorMessage = "Completa todos los campos")
                return@launch
            }

            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            when (val result = authRepository.login(normalizedEmail, password)) {
                is ApiResult.Success -> {
                    _state.value = _state.value.copy(isLoading = false, errorMessage = null)
                    _events.emit(AuthEvent.LoginSuccess)
                }
                is ApiResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                    _events.emit(AuthEvent.Error(result.message))
                }
            }
        }
    }

    fun register(name: String, email: String, password: String, confirm: String) {
        viewModelScope.launch {
            val normalizedName = name.trim()
            val normalizedEmail = email.trim().lowercase()

            if (normalizedName.isBlank() || normalizedEmail.isBlank() || password.isBlank()) {
                _state.value = _state.value.copy(errorMessage = "Completa todos los campos")
                return@launch
            }
            if (password != confirm) {
                _state.value = _state.value.copy(errorMessage = "Las contraseñas no coinciden")
                return@launch
            }

            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            when (val result = authRepository.register(normalizedName, normalizedEmail, password)) {
                is ApiResult.Success -> {
                    sessionManager.saveUserSession(result.data.id)
                    _state.value = _state.value.copy(isLoading = false, errorMessage = null)
                    _events.emit(AuthEvent.RegisterSuccess)
                }
                is ApiResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                    _events.emit(AuthEvent.Error(result.message))
                }
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }

    fun hasActiveSession(): Boolean = sessionManager.isLoggedIn()
}
