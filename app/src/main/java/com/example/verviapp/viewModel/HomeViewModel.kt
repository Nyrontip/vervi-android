package com.example.verviapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.verviapp.R
import com.example.verviapp.viewModel.state.HomeState
import com.example.verviapp.viewModel.state.Service
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    // init se ejecuta automáticamente al crear el ViewModel
    init { loadServices() }

    private fun loadServices() {
        _state.value = _state.value.copy(isLoading = true)
        // TODO: reemplazar con llamada a API real
        _state.value = _state.value.copy(isLoading = false, services = hardcodedServices())
    }

    // Búsqueda y filtro por categoría los maneja el backend
    // Estos métodos actualizan el estado y luego llamarán a la API con los parámetros
    fun onCategoryChange(category: String) {
        // TODO: loadServices(category = category) cuando haya API
    }

    fun onSearchChange(query: String) {
        // TODO: loadServices(search = query) cuando haya API
    }

    // Datos quemados — se eliminan cuando haya API
    private fun hardcodedServices() = listOf(
        Service("1", "Reparación de tubería cocina", "Bogotá, Chapinero",
            "\$50.000 COP", 3, true, R.drawable.login_hero, "Plomería"),
        Service("2", "Limpieza profunda de apartamento", "Bogotá, Cedritos",
            "\$85.000 COP", 8, false, R.drawable.login_hero, "Limpieza")
    )
}