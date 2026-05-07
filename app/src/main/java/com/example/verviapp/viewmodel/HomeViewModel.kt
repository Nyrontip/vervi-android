package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.repository.ApiResult
import com.example.verviapp.data.repository.RequestRepository
import com.example.verviapp.viewmodel.state.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val requestRepository: RequestRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

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
        viewModelScope.launch {
            when (val result = requestRepository.loadCategories()) {
                is ApiResult.Success -> {
                    val categories = result.data.map { it.name }
                    _state.value = _state.value.copy(categories = listOf("Todos") + categories)
                }
                is ApiResult.Error -> {
                    _state.value = _state.value.copy(categories = listOf("Todos"))
                }
            }
        }
    }

    private fun executeSearch() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            when (val result = requestRepository.loadHomeRequests(
                searchQuery = _state.value.searchQuery.trim(),
                category = _state.value.selectedCategory
            )) {
                is ApiResult.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = null,
                        services = result.data
                    )
                }
                is ApiResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }
}
