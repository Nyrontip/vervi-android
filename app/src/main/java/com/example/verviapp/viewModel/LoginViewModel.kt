package com.example.verviapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.verviapp.viewModel.state.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {

    // _state es privado — solo el ViewModel puede modificarlo
    private val _state = MutableStateFlow(AuthState())
    // state es público — la UI solo puede leerlo con collectAsState()
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.value = _state.value.copy(errorMessage = "Complete all fields")
            return
        }
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)

        // TODO: reemplazar con llamada a API real
        if (email == "test@vervi.com" && password == "123456") {
            _state.value = _state.value.copy(isLoading = false, loginSuccess = true)
        } else {
            _state.value = _state.value.copy(isLoading = false,
                errorMessage = "Invalid email or password")
        }
    }

    fun register(name: String, email: String, password: String, confirm: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _state.value = _state.value.copy(errorMessage = "Complete all fields")
            return
        }
        if (password != confirm) {
            _state.value = _state.value.copy(errorMessage = "Passwords do not match")
            return
        }
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)

        // TODO: reemplazar con llamada a API real
        _state.value = _state.value.copy(isLoading = false, registerSuccess = true)
    }

    // Resetea el estado al valor inicial — se llama después de navegar
    fun resetState() { _state.value = AuthState() }
}