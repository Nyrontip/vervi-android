package com.example.verviapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.dao.UserDao
import com.example.verviapp.data.entity.UserEntity
import com.example.verviapp.viewModel.state.AuthState
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
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userDao: UserDao
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

            try {
                val user = userDao.getUserByEmail(normalizedEmail)
                if (user == null || user.password != password) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "Correo o contraseña inválidos"
                    )
                    return@launch
                }

                _state.value = _state.value.copy(isLoading = false, errorMessage = null)
                _events.emit(AuthEvent.LoginSuccess)
            } catch (t: Throwable) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = t.message ?: "No se pudo iniciar sesión"
                )
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

            try {
                val existingUser = userDao.getUserByEmail(normalizedEmail)
                if (existingUser != null) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "Este correo ya está registrado"
                    )
                    return@launch
                }

                val insertResult = userDao.insertUser(
                    UserEntity(
                        name = normalizedName,
                        email = normalizedEmail,
                        password = password
                    )
                )

                if (insertResult == -1L) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "No se pudo registrar el usuario"
                    )
                    return@launch
                }

                _state.value = _state.value.copy(isLoading = false, errorMessage = null)
                _events.emit(AuthEvent.RegisterSuccess)
            } catch (t: Throwable) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = t.message ?: "No se pudo registrar"
                )
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }
}
