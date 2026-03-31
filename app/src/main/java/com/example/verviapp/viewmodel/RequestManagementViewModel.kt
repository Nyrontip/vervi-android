package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.model.RequestItem
import com.example.verviapp.repository.RequestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RequestUiState(
    val selectedTab: Int = 0,
    val requests: List<RequestItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class RequestEvent {
    data class OpenRequest(val item: RequestItem) : RequestEvent()
    object CreateNew : RequestEvent()
}

@HiltViewModel
class RequestManagementViewModel @Inject constructor(
    private val repository: RequestRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RequestUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<RequestEvent>()
    val events = _events.asSharedFlow()

    init {
        loadRequests()
    }

    fun selectTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
        val activeOnly = when (index) {
            0 -> true
            1 -> false
            else -> null
        }
        loadRequests(activeOnly)
    }

    fun onOpen(item: RequestItem) {
        viewModelScope.launch { _events.emit(RequestEvent.OpenRequest(item)) }
    }

    fun onCreateNew() {
        viewModelScope.launch { _events.emit(RequestEvent.CreateNew) }
    }

    fun refresh() { loadRequests() }

    private fun loadRequests(activeOnly: Boolean? = null) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                val data = repository.getRequests(activeOnly)
                _uiState.value = _uiState.value.copy(requests = data, isLoading = false)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(error = t.message ?: "Error desconocido", isLoading = false)
            }
        }
    }
}

