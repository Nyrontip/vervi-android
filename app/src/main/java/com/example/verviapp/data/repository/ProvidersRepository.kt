package com.example.verviapp.data.repository

import com.example.verviapp.data.remote.VerviApi
import com.example.verviapp.data.remote.dto.*
import com.example.verviapp.viewmodel.state.Provider
import com.example.verviapp.viewmodel.state.ProvidersState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProvidersRepository @Inject constructor(
    private val api: VerviApi
) {
    private val _state = MutableStateFlow(ProvidersState())
    val state: StateFlow<ProvidersState> = _state.asStateFlow()

    private val currencyFormatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"))

    suspend fun loadProviders(
        searchQuery: String = "",
        category: String = "Todos"
    ): ApiResult<List<Provider>> {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)

        return try {
            val response = api.getUsers()
            if (response.isSuccessful) {
                val allUsers = response.body() ?: emptyList()

                val filtered = allUsers.filter { user ->
                    val matchesSearch = searchQuery.isBlank() ||
                            user.name.lowercase().contains(searchQuery.lowercase()) ||
                            user.bio?.lowercase()?.contains(searchQuery.lowercase()) == true ||
                            user.location?.lowercase()?.contains(searchQuery.lowercase()) == true

                    val matchesCategory = category == "Todos" ||
                            user.categories?.any { it.name == category } == true

                    val isProvider = user.isProvider

                    matchesSearch && matchesCategory && isProvider
                }

                val providers = filtered.sortedByDescending { it.rating }.map { it.toUiProvider() }
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = null,
                    providers = providers
                )
                ApiResult.Success(providers)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error al cargar prestadores"
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = errorMsg
                )
                ApiResult.Error(errorMsg, response.code())
            }
        } catch (e: Exception) {
            val errorMsg = e.message ?: "Error de conexión"
            _state.value = _state.value.copy(
                isLoading = false,
                errorMessage = errorMsg
            )
            ApiResult.Error(errorMsg)
        }
    }

    fun onSearchChange(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
    }

    fun onCategoryChange(category: String) {
        _state.value = _state.value.copy(selectedCategory = category)
    }

    private fun UserDto.toUiProvider(): Provider {
        val formattedPrice = suggestedPriceCop?.let { "\$${currencyFormatter.format(it)} COP" } ?: "A convenir"
        val specialtyText = categories?.firstOrNull()?.name ?: "Sin categoría"

        return Provider(
            id = id,
            name = name,
            specialty = specialtyText.uppercase(),
            price = formattedPrice,
            rating = rating,
            reviewCount = reviewCount,
            imageUrl = photoUrl ?: "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=600&h=600&fit=crop"
        )
    }
}