package com.example.verviapp.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import com.example.verviapp.R
import com.example.verviapp.data.dao.UserDao
import com.example.verviapp.data.entity.UserWithCategories
import com.example.verviapp.viewmodel.state.Provider
import com.example.verviapp.viewmodel.state.ProvidersState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PrestadoresViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {

    private val _state = MutableStateFlow(ProvidersState())
    val state: StateFlow<ProvidersState> = _state.asStateFlow()
    private var searchJob: Job? = null
    private var categoriesJob: Job? = null
    private val currencyFormatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"))

    init {
        observeCategories()
        executeSearch()
    }

    fun onSearchChange(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
    }

    fun onCategoryChange(category: String) {
        _state.value = _state.value.copy(selectedCategory = category)
        executeSearch()
    }

    fun onSearchDone() {
        executeSearch()
    }

    private fun observeCategories() {
        categoriesJob?.cancel()
        categoriesJob = viewModelScope.launch {
            runCatching { userDao.getCategoryNames() }
                .onSuccess { categories ->
                    _state.value = _state.value.copy(categories = listOf("Todos") + categories)
                }
                .onFailure {
                    _state.value = _state.value.copy(categories = listOf("Todos"))
                }
        }
    }

    private fun executeSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            userDao
                .observeProviders(
                    text = _state.value.searchQuery.trim(),
                    category = _state.value.selectedCategory
                )
                .onStart {
                    _state.value = _state.value.copy(isLoading = true, errorMessage = null)
                }
                .catch { throwable ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "No se pudo cargar la lista de prestadores"
                    )
                }
                .collect { providers ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = null,
                        providers = providers.map { it.toUiModel() }
                    )
                }
        }
    }

    private fun UserWithCategories.toUiModel(): Provider {
        val formattedPrice = user.suggestedPriceCop?.let { value ->
            "\$${currencyFormatter.format(value)} COP"
        } ?: "A convenir"

        val specialtyText = categories.firstOrNull()?.name ?: "Sin categoria"

        return Provider(
            id = user.id,
            name = user.name,
            specialty = specialtyText.uppercase(),
            price = formattedPrice,
            rating = user.rating,
            reviewCount = user.reviewCount,
            imageRes = R.drawable.login_hero
        )
    }
}
