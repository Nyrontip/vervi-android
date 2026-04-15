package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.model.RequestConfirmUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class RequestConfirmEvent {
    object Confirmed : RequestConfirmEvent()
}

class RequestConfirmViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RequestConfirmUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<RequestConfirmEvent>()
    val events = _events.asSharedFlow()

    fun load(requestId: Int) {
        if (_uiState.value.requestId == requestId) return
        _uiState.value = _uiState.value.copy(requestId = requestId, error = null)
    }

    fun onPaymentReceiptChecked(checked: Boolean) {
        _uiState.value = _uiState.value.copy(paymentReceiptConfirmed = checked, error = null)
    }

    fun confirmRequest() {
        val state = _uiState.value
        if (!state.paymentReceiptConfirmed) {
            _uiState.value = state.copy(error = "Debes confirmar el recibo de pago")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, error = null)
            // Este proyecto no tiene capa de persistencia para solicitudes aun.
            _uiState.value = _uiState.value.copy(isSubmitting = false)
            _events.emit(RequestConfirmEvent.Confirmed)
        }
    }
}

