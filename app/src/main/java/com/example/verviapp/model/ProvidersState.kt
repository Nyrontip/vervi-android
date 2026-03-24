package com.example.verviapp.model

// Estado completo de la pantalla de Prestadores
data class ProvidersState(
    val providers: List<Provider> = emptyList(),
    val isLoading: Boolean = false
)