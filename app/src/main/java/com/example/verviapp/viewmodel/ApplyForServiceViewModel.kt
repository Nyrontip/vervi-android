package com.example.verviapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.net.Uri
import com.example.verviapp.data.remote.dto.ApplicationCreateRequest
import com.example.verviapp.data.repository.ApiResult
import com.example.verviapp.data.repository.ImageUploadRepository
import com.example.verviapp.data.repository.RequestRepository
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
    private val requestRepository: RequestRepository,
    private val imageUploadRepository: ImageUploadRepository,
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
            when (val result = requestRepository.getRequestDetail(requestId)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        requestId = requestId,
                        requestTitle = result.data.title,
                        error = null
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        requestId = requestId,
                        error = result.message
                    )
                }
            }
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
                val providerUserId = sessionManager.getLoggedInUserId() ?: run {
                    _uiState.value = _uiState.value.copy(isSubmitting = false, error = "Debes iniciar sesión")
                    return@launch
                }

                // Upload evidence image if selected
                var evidenceImageUrl: String? = null
                state.evidenceUri?.let { uriStr ->
                    val uri = Uri.parse(uriStr)
                    when (val uploadResult = imageUploadRepository.uploadImage(uri)) {
                        is ApiResult.Success -> evidenceImageUrl = uploadResult.data.secureUrl
                        is ApiResult.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isSubmitting = false,
                                error = "Error al subir imagen de evidencia: ${uploadResult.message}"
                            )
                            return@launch
                        }
                    }
                }

                val result = requestRepository.createApplication(
                    ApplicationCreateRequest(
                        requestId = safeRequestId,
                        providerUserId = providerUserId,
                        presentationMessage = state.presentationMessage.trim(),
                        proposedPriceCop = parsedPrice,
                        immediateAvailability = state.immediateAvailability,
                        evidenceImageUrl = evidenceImageUrl
                    )
                )

                when (result) {
                    is ApiResult.Success -> {
                        _uiState.value = _uiState.value.copy(isSubmitting = false)
                        _events.emit(ApplyForServiceEvent.Submitted)
                    }
                    is ApiResult.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isSubmitting = false,
                            error = result.message
                        )
                    }
                }
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    error = t.message ?: "Error desconocido"
                )
            }
        }
    }
}
