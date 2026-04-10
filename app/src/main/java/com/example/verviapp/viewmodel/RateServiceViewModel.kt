package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.dao.ServiceDao
import com.example.verviapp.data.entity.ReviewEntity
import com.example.verviapp.data.repository.SampleData
import com.example.verviapp.viewmodel.state.RateServiceUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RateServiceEvent {
	object Submitted : RateServiceEvent()
}

@HiltViewModel
class RateServiceViewModel @Inject constructor(
	private val serviceDao: ServiceDao
) : ViewModel() {

	private val _uiState = MutableStateFlow(RateServiceUiState())
	val uiState: StateFlow<RateServiceUiState> = _uiState.asStateFlow()
	private val _events = MutableSharedFlow<RateServiceEvent>()
	val events = _events.asSharedFlow()

	fun load(serviceId: Int) {
		viewModelScope.launch {
			_uiState.value = _uiState.value.copy(isLoading = true, error = null)
			try {
				val dto = serviceDao.getRateServiceRow(serviceId, CURRENT_USER_ID)
				if (dto == null) {
					_uiState.value = _uiState.value.copy(
						isLoading = false,
						error = "No se encontró el servicio para calificar"
					)
					return@launch
				}

				_uiState.value = _uiState.value.copy(
					serviceId = dto.serviceId,
					counterpartUserId = dto.counterpartUserId,
					counterpartName = dto.counterpartName,
					counterpartAvatarUrl = dto.counterpartAvatarUrl.orEmpty(),
					rating = dto.existingRating ?: 4,
					comment = dto.existingComment.orEmpty(),
					imageUri = dto.existingEvidenceImageUrl,
					isLoading = false,
					error = null
				)
			} catch (e: Exception) {
				_uiState.value = _uiState.value.copy(
					isLoading = false,
					error = e.message ?: "No se pudo cargar la calificación"
				)
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

		if (serviceId == null || counterpartUserId == null) {
			_uiState.value = s.copy(error = "No hay contexto del servicio para enviar la calificación")
			return
		}

		viewModelScope.launch {
			_uiState.value = s.copy(isSubmitting = true, error = null)
			try {
				val latestReviewId = serviceDao.getLatestReviewId(serviceId, CURRENT_USER_ID)
				serviceDao.insertReview(
					ReviewEntity(
						id = latestReviewId ?: 0,
						serviceId = serviceId,
						reviewerUserId = CURRENT_USER_ID,
						reviewedUserId = counterpartUserId,
						rating = s.rating,
						comment = s.comment.ifBlank { null },
						evidenceImageUrl = s.imageUri
					)
				)
				_uiState.value = s.copy(isSubmitting = false, error = null)
				_events.emit(RateServiceEvent.Submitted)
			} catch (e: Exception) {
				_uiState.value = s.copy(isSubmitting = false, error = e.message)
			}
		}
	}

	fun refresh() {
		_uiState.value.serviceId?.let { load(it) }
	}

	private companion object {
		const val CURRENT_USER_ID = SampleData.SERVICE_LOCAL_USER_ID
	}
}


