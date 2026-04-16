package com.example.verviapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.dao.RequestDao
import com.example.verviapp.data.dao.ServiceApplicationDao
import com.example.verviapp.data.entity.ServiceApplicationEntity
import com.example.verviapp.data.repository.SampleData
import com.example.verviapp.data.session.SessionManager
import com.example.verviapp.viewmodel.state.ApplyForServiceUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ApplyForServiceEvent {
    object Submitted : ApplyForServiceEvent()
}

@HiltViewModel
class ApplyForServiceViewModel @Inject constructor(
    private val requestDao: RequestDao,
    private val serviceApplicationDao: ServiceApplicationDao,
    private val sessionManager: SessionManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val requestIdArg: Int = savedStateHandle.get<Int>("requestId")
        ?: savedStateHandle.get<String>("requestId")?.toIntOrNull()
        ?: 0

    private val _uiState = MutableStateFlow(ApplyForServiceUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ApplyForServiceEvent>()
    val events = _events.asSharedFlow()

    init {
        if (requestIdArg > 0) {
            load(requestIdArg)
        }
    }

    fun load(requestId: Int) {
        viewModelScope.launch {
            val request = requestDao.getRequestById(requestId)
            _uiState.value = _uiState.value.copy(
                requestId = requestId,
                requestTitle = request?.title.orEmpty(),
                error = null
            )
        }
    }

    fun onPresentationMessageChange(value: String) {
        _uiState.value = _uiState.value.copy(presentationMessage = value, error = null)
    }

    fun onProposedPriceChange(value: String) {
        _uiState.value = _uiState.value.copy(proposedPrice = value, error = null)
    }

    fun onEvidenceSelected(uri: String?) {
        _uiState.value = _uiState.value.copy(evidenceUri = uri)
    }

    fun onImmediateAvailabilityChange(value: Boolean) {
        _uiState.value = _uiState.value.copy(immediateAvailability = value)
    }

    fun submit() {
        val state = _uiState.value
        val requestId = state.requestId
        val parsedPrice = state.proposedPrice
            .replace("$", "")
            .replace(".", "")
            .replace(",", "")
            .trim()
            .toLongOrNull()

        val validationError = when {
            requestId == null -> "No se pudo identificar la solicitud"
            state.presentationMessage.isBlank() -> "El mensaje de presentacion es obligatorio"
            state.proposedPrice.isBlank() -> "Ingresa un precio propuesto"
            parsedPrice == null || parsedPrice <= 0L -> "Ingresa un precio valido"
            else -> null
        }

        if (validationError != null) {
            _uiState.value = state.copy(error = validationError)
            return
        }

        val safeRequestId = requestId ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, error = null)
            try {
                val providerUserId = sessionManager.getLoggedInUserId() ?: SampleData.SERVICE_LOCAL_USER_ID
                val ok = serviceApplicationDao.submitApplication(
                    ServiceApplicationEntity(
                        requestId = safeRequestId,
                        providerUserId = providerUserId,
                        presentationMessage = state.presentationMessage.trim(),
                        proposedPriceCop = parsedPrice,
                        evidenceUri = state.evidenceUri,
                        immediateAvailability = state.immediateAvailability,
                        status = "PENDING"
                    )
                )

                if (!ok) {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        error = "No se pudo registrar la postulacion"
                    )
                    return@launch
                }

                _uiState.value = _uiState.value.copy(isSubmitting = false)
                _events.emit(ApplyForServiceEvent.Submitted)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    error = t.message ?: "Error desconocido"
                )
            }
        }
    }
}


