package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.model.RequestDetail
import com.example.verviapp.repository.RequestRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RequestDetailsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val request: RequestDetail? = null
)

class RequestDetailsViewModel() : ViewModel() {

    private val repository = RequestRepositoryImpl()
    private val requestId: String = "sample-1"

    private val _uiState = MutableStateFlow(RequestDetailsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val data = repository.getRequest(requestId)
                _uiState.value = _uiState.value.copy(request = data, isLoading = false)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(error = t.message ?: "Error desconocido", isLoading = false)
            }
        }
    }
}


