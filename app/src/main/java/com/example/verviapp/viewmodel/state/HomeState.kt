package com.example.verviapp.viewmodel.state

// Estado completo de la pantalla Home
data class HomeState(
    val services: List<Service> = emptyList(),
    val isLoading: Boolean = false
)