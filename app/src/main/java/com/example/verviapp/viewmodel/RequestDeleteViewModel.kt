package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.repository.ApiResult
import com.example.verviapp.data.repository.RequestRepository
import com.example.verviapp.viewmodel.state.RequestDeleteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RequestDeleteEvent {
    object Deleted : RequestDeleteEvent()
}

@HiltViewModel
class RequestDeleteViewModel @Inject constructor(
    private val requestRepository: RequestRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RequestDeleteUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<RequestDeleteEvent>()
    val events = _events.asSharedFlow()

    fun load(requestId: Int) {
        if (_uiState.value.requestId == requestId) return
        _uiState.value = _uiState.value.copy(requestId = requestId, error = null)
    }

    fun deleteRequest() {
        val state = _uiState.value
        val requestId = state.requestId

        if (requestId == null) {
            _uiState.value = state.copy(error = "No se pudo identificar la solicitud")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, error = null)
            when (val result = requestRepository.deleteRequest(requestId)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(isSubmitting = false)
                    _events.emit(RequestDeleteEvent.Deleted)
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        error = result.message
                    )
                }
            }
        }
    }
}
