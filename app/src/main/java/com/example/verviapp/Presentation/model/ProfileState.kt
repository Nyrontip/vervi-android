package com.example.verviapp.Presentation.model

// Estado completo de la pantalla de Perfil
data class ProfileState(
    val user: User = User(),
    val isLoading: Boolean = false
)