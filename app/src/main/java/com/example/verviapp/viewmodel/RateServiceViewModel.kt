package com.example.verviapp.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.repository.ApiResult
import com.example.verviapp.data.repository.ImageUploadRepository
import com.example.verviapp.data.repository.ReviewsRepository
import com.example.verviapp.data.repository.ServiceRepository
import com.example.verviapp.data.session.SessionManager
import com.example.verviapp.viewmodel.state.RateServiceUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RateServiceEvent {
    object Submitted : RateServiceEvent()
}

@HiltViewModel
class RateServiceViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val reviewsRepository: ReviewsRepository,
    private val imageUploadRepository: ImageUploadRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(RateServiceUiState())
    val uiState: StateFlow<RateServiceUiState> = _uiState.asStateFlow()
    private val _events = MutableSharedFlow<RateServiceEvent>()
    val events = _events.asSharedFlow()

    fun load(serviceId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = serviceRepository.getServiceById(serviceId)) {
                is ApiResult.Success -> {
                    val dto = result.data
                    val currentUserId = sessionManager.getLoggedInUserId()
                    // Counterpart is the other participant in the service
                    val counterpart = if (currentUserId == dto.clientUserId) dto.provider else dto.client
                    _uiState.value = _uiState.value.copy(
                        serviceId = dto.id,
                        counterpartUserId = if (currentUserId == dto.clientUserId) dto.providerUserId else dto.clientUserId,
                        counterpartName = counterpart?.name ?: "Usuario",
                        counterpartAvatarUrl = counterpart?.photoUrl.orEmpty(),
                        isLoading = false,
                        error = null
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun onRatingSelected(value: Int) {
        _uiState.value = _uiState.value.copy(rating = value)
    }

    fun onCommentChange(text: String) {
        _uiState.value = _uiState.value.copy(comment = text)
    }

    fun onAttachImage(uri: String?) {
        _uiState.value = _uiState.value.copy(imageUri = uri)
    }

    fun submitRating() {
        val s = _uiState.value
        val serviceId = s.serviceId
        val counterpartUserId = s.counterpartUserId
        val reviewerUserId = sessionManager.getLoggedInUserId()

        if (serviceId == null || counterpartUserId == null) {
            _uiState.value = s.copy(error = "No hay contexto del servicio para enviar la calificación")
            return
        }
        if (reviewerUserId == null) {
            _uiState.value = s.copy(error = "Debes iniciar sesión para calificar")
            return
        }

        viewModelScope.launch {
            _uiState.value = s.copy(isSubmitting = true, error = null)
            try {
                // Upload evidence image if provided
                var evidenceUrl: String? = null
                s.imageUri?.let { uriStr ->
                    val uri = Uri.parse(uriStr)
                    when (val uploadResult = imageUploadRepository.uploadImage(uri)) {
                        is ApiResult.Success -> evidenceUrl = uploadResult.data.secureUrl
                        is ApiResult.Error -> {
                            _uiState.value = s.copy(
                                isSubmitting = false,
                                error = "Error al subir imagen: ${uploadResult.message}"
                            )
                            return@launch
                        }
                    }
                }

                when (val result = reviewsRepository.createReview(
                    serviceId = serviceId,
                    reviewerUserId = reviewerUserId,
                    reviewedUserId = counterpartUserId,
                    rating = s.rating,
                    comment = s.comment.takeIf { it.isNotBlank() },
                    evidenceImageUrl = evidenceUrl
                )) {
                    is ApiResult.Success -> {
                        _uiState.value = s.copy(isSubmitting = false, error = null)
                        _events.emit(RateServiceEvent.Submitted)
                    }
                    is ApiResult.Error -> {
                        _uiState.value = s.copy(isSubmitting = false, error = result.message)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = s.copy(isSubmitting = false, error = e.message)
            }
        }
    }

    fun refresh() {
        _uiState.value.serviceId?.let { load(it) }
    }
}
