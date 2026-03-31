package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.model.RatingData
import com.example.verviapp.repository.RatingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RateUiState(
	val rating: Int = 4,
	val comment: String = "",
	val imageUri: String? = null,
	val isSubmitting: Boolean = false,
	val error: String? = null
)

class RateServiceViewModel(
	private val repository: RatingsRepository
) : ViewModel() {

	private val _uiState = MutableStateFlow(RateUiState())
	val uiState: StateFlow<RateUiState> = _uiState.asStateFlow()

	fun onRatingSelected(value: Int) {
		_uiState.value = _uiState.value.copy(rating = value)
	}

	fun onCommentChange(text: String) {
		_uiState.value = _uiState.value.copy(comment = text)
	}

	fun onAttachImage(uri: String?) {
		_uiState.value = _uiState.value.copy(imageUri = uri)
	}

	fun submitRating(providerName: String) {
		val s = _uiState.value
		viewModelScope.launch {
			_uiState.value = s.copy(isSubmitting = true)
			try {
				val ratingData = RatingData(providerName, s.rating, s.comment, s.imageUri)
				repository.submitRating(ratingData)
				_uiState.value = s.copy(isSubmitting = false)
			} catch (e: Exception) {
				_uiState.value = s.copy(isSubmitting = false, error = e.message)
			}
		}
	}

	fun refresh() {
		// no-op por ahora
	}
}


