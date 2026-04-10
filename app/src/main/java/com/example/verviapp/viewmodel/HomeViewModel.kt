package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.dao.CategoryDao
import com.example.verviapp.data.dao.HomeRequestRow
import com.example.verviapp.data.dao.RequestDao
import com.example.verviapp.viewmodel.state.HomeState
import com.example.verviapp.viewmodel.state.Service
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val requestDao: RequestDao,
    private val categoryDao: CategoryDao
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()
    private var requestsJob: Job? = null
    private var categoriesJob: Job? = null
    private val currencyFormatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"))

    init {
        loadCategories()
        executeSearch()
    }

    fun onCategoryChange(category: String) {
        _state.value = _state.value.copy(selectedCategory = category)
        executeSearch()
    }

    fun onSearchChange(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
    }

    fun onSearchDone() {
        executeSearch()
    }

    private fun loadCategories() {
        categoriesJob?.cancel()
        categoriesJob = viewModelScope.launch {
            runCatching { categoryDao.getCategoryNames() }
                .onSuccess { categories ->
                    _state.value = _state.value.copy(categories = listOf("Todos") + categories)
                }
                .onFailure {
                    _state.value = _state.value.copy(categories = listOf("Todos"))
                }
        }
    }

    private fun executeSearch() {
        requestsJob?.cancel()
        requestsJob = viewModelScope.launch {
            requestDao.observeHomeRequests(
                text = _state.value.searchQuery.trim(),
                category = _state.value.selectedCategory
            )
                .onStart {
                    _state.value = _state.value.copy(isLoading = true, errorMessage = null)
                }
                .catch { throwable ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "No se pudieron cargar las solicitudes"
                    )
                }
                .collect { rows ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = null,
                        services = rows.map { row -> row.toUiModel() }
                    )
                }
        }
    }

    private fun HomeRequestRow.toUiModel(): Service {
        val formattedPrice = budgetCop?.let { value ->
            "\$${currencyFormatter.format(value)} COP"
        } ?: "A convenir"

        return Service(
            id = id,
            title = title,
            location = location,
            price = formattedPrice,
            applicationCount = applicationCount,
            isUrgent = isUrgent,
            imageUrl = imageUrl,
            category = categoryName ?: "Sin categoría"
        )
    }
}
