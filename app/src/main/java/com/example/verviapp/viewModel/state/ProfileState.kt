package com.example.verviapp.viewModel.state

// Estado completo de la pantalla de Perfil
data class ProfileState(
    val user: User = User(),
    val isLoading: Boolean = false
)