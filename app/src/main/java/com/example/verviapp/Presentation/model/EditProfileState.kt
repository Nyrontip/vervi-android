package com.example.verviapp.Presentation.model

// Estado completo del formulario de edición de perfil
data class EditProfileState(
    val name: String = "Juan Pérez",
    val bio: String = "",
    val price: String = "50.000",
    val location: String = "Bogotá",
    val isProvider: Boolean = true,
    val categories: List<String> = listOf("Tutorías", "Diseño"),
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null
)