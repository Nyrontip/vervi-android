package com.example.verviapp.viewmodel.state

// Estado completo de la pantalla de Prestadores
data class ProvidersState(
    val providers: List<Provider> = emptyList(),
    val categories: List<String> = listOf("Todos"),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedCategory: String = "Todos",
    val errorMessage: String? = null
)
