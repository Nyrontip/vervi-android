package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.verviapp.R
import com.example.verviapp.model.Provider
import com.example.verviapp.model.ProvidersState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PrestadoresViewModel : ViewModel() {

    private val _state = MutableStateFlow(ProvidersState())
    val state: StateFlow<ProvidersState> = _state.asStateFlow()

    init { loadProviders() }

    private fun loadProviders() {
        _state.value = _state.value.copy(isLoading = true)
        // TODO: reemplazar con llamada a API real
        _state.value = _state.value.copy(isLoading = false, providers = hardcodedProviders())
    }

    fun onCategoryChange(category: String) {
        // TODO: loadProviders(category = category) cuando haya API
    }

    fun onSearchChange(query: String) {
        // TODO: loadProviders(search = query) cuando haya API
    }

    private fun hardcodedProviders() = listOf(
        Provider("1", "Carlos Ruiz",     "PLOMERO",       "\$80.000 COP", 4.9f, 48, R.drawable.login_hero),
        Provider("2", "Mateo Gómez",     "CARPINTERO",    "\$75.000 COP", 4.7f, 32, R.drawable.login_hero),
        Provider("3", "Andrés Restrepo", "ELECTRICISTA",  "\$95.000 COP", 4.9f, 15, R.drawable.login_hero)
    )
}