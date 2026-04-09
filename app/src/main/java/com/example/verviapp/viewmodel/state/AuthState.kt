package com.example.verviapp.viewmodel.state

// Estado de la pantalla de login/registro
// sealed class: cada caso es un tipo distinto de resultado posible
// La UI observa este estado y reacciona según el caso que llegue
data class AuthState(
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val registerSuccess: Boolean = false,
    val errorMessage: String? = null   // null = sin error
)