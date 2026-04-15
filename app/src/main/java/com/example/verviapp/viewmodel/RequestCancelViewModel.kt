package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.dao.RequestDao
import com.example.verviapp.viewmodel.state.RequestCancelUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RequestCancelEvent {
    object Cancelled : RequestCancelEvent()
}

@HiltViewModel
class RequestCancelViewModel @Inject constructor(
    private val requestDao: RequestDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(RequestCancelUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<RequestCancelEvent>()
    val events = _events.asSharedFlow()

    fun load(requestId: Int) {
        if (_uiState.value.requestId == requestId) return
        _uiState.value = _uiState.value.copy(requestId = requestId, error = null)
    }

    fun onReasonSelected(reason: String) {
        _uiState.value = _uiState.value.copy(selectedReason = reason, error = null)
    }

    fun onAdditionalDetailsChange(details: String) {
        _uiState.value = _uiState.value.copy(additionalDetails = details)
    }

    fun cancelRequest() {
        val state = _uiState.value
        val requestId = state.requestId

        if (requestId == null) {
            _uiState.value = state.copy(error = "No se pudo identificar la solicitud")
            return
        }

        if (state.selectedReason.isBlank()) {
            _uiState.value = state.copy(error = "Selecciona un motivo de cancelacion")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, error = null)
            try {
                val now = System.currentTimeMillis()
                val updatedRows = requestDao.cancelRequest(
                    requestId = requestId,
                    status = "Cancelada",
                    buttonText = "Ver",
                    closedAt = now,
                    updatedAt = now
                )

                if (updatedRows == 0) {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        error = "No se pudo cancelar la solicitud"
                    )
                    return@launch
                }

                _uiState.value = _uiState.value.copy(isSubmitting = false)
                _events.emit(RequestCancelEvent.Cancelled)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    error = t.message ?: "Error desconocido"
                )
            }
        }
    }
}

