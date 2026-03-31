package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.model.ServiceHistoryItem
import com.example.verviapp.repository.ServiceHistoryRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ServiceHistoryUiState(
	val selectedTab: Int = 0,
	val services: List<ServiceHistoryItem> = emptyList(),
	val isLoading: Boolean = false,
	val error: String? = null
)

sealed class ServiceHistoryEvent {
	data class OpenDetails(val item: ServiceHistoryItem) : ServiceHistoryEvent()
	data class OpenRate(val item: ServiceHistoryItem) : ServiceHistoryEvent()
}

class ServiceHistoryViewModel(private val repository: ServiceHistoryRepository) : ViewModel() {

	private val _uiState = MutableStateFlow(ServiceHistoryUiState())
	val uiState = _uiState.asStateFlow()

	private val _navigationEvents = MutableSharedFlow<ServiceHistoryEvent>()
	val navigationEvents = _navigationEvents.asSharedFlow()

	init {
		loadHistory()
	}

	fun selectTab(index: Int) {
		_uiState.value = _uiState.value.copy(selectedTab = index)
		// In a real implementation, you might filter services based on tab
	}

	fun onServiceClick(item: ServiceHistoryItem) {
		viewModelScope.launch {
			_navigationEvents.emit(ServiceHistoryEvent.OpenDetails(item))
		}
	}

	fun onRate(item: ServiceHistoryItem) {
		viewModelScope.launch {
			_navigationEvents.emit(ServiceHistoryEvent.OpenRate(item))
		}
	}

	fun refresh() {
		loadHistory()
	}

	private fun loadHistory() {
		viewModelScope.launch {
			try {
				_uiState.value = _uiState.value.copy(isLoading = true, error = null)
				val data = repository.getHistory()
				_uiState.value = _uiState.value.copy(services = data, isLoading = false)
			} catch (t: Throwable) {
				_uiState.value = _uiState.value.copy(error = t.message ?: "Error desconocido", isLoading = false)
			}
		}
	}
}


