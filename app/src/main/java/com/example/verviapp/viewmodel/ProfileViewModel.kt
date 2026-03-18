package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.verviapp.model.ProfileState
import com.example.verviapp.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init { loadProfile() }

    private fun loadProfile() {
        _state.value = _state.value.copy(isLoading = true)
        // TODO: reemplazar con llamada a API real o SharedPreferences
        _state.value = _state.value.copy(
            isLoading = false,
            user = User(
                id             = "1",
                name           = "Juan Pérez",
                email          = "juan@vervi.com",
                bio            = "Estudiante de Ingeniería apasionado por el servicio técnico.",
                location       = "Bogotá, Colombia",
                rating         = 4.9f,
                reviewCount    = 124,
                suggestedPrice = "\$50.000",
                categories     = listOf("Plomería", "Carpintería"),
                isProvider     = true,
                projectCount   = 24,
                requestCount   = 12
            )
        )
    }
}