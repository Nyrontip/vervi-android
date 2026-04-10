package com.example.verviapp.viewmodel.state

// Estado completo del formulario de edición de perfil
data class EditProfileState(
    val name: String = "",
    val bio: String = "",
    val price: String = "",
    val location: String = "",
    val isProvider: Boolean = false,
    val categories: List<String> = emptyList(),
    val availableCategories: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null
)
