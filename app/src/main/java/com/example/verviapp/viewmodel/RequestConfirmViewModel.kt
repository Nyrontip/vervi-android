package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.repository.ApiResult
import com.example.verviapp.data.repository.RequestRepository
import com.example.verviapp.viewmodel.state.RequestConfirmUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RequestConfirmEvent {
    object Confirmed : RequestConfirmEvent()
}

@HiltViewModel
class RequestConfirmViewModel @Inject constructor(
    private val requestRepository: RequestRepository
) : ViewModel() {

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
        val requestId = state.requestId

        if (requestId == null) {
            _uiState.value = state.copy(error = "No se pudo identificar la solicitud")
            return
        }

        if (!state.paymentReceiptConfirmed) {
            _uiState.value = state.copy(error = "Debes confirmar el recibo de pago")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, error = null)
            when (val result = requestRepository.updateRequestStatus(requestId, "COMPLETED")) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(isSubmitting = false)
                    _events.emit(RequestConfirmEvent.Confirmed)
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
