package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.dao.ServiceDao
import com.example.verviapp.data.repository.SampleData
import com.example.verviapp.viewmodel.state.ServiceHistoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

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

@HiltViewModel
class ServiceHistoryViewModel @Inject constructor(
	private val serviceDao: ServiceDao
) : ViewModel() {

	private val _uiState = MutableStateFlow(ServiceHistoryUiState())
	val uiState = _uiState.asStateFlow()

	private val _navigationEvents = MutableSharedFlow<ServiceHistoryEvent>()
	val navigationEvents = _navigationEvents.asSharedFlow()

	private var loadJob: Job? = null

	init {
		loadHistory()
	}

	fun selectTab(index: Int) {
		_uiState.value = _uiState.value.copy(selectedTab = index)
		loadHistory()
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
		loadJob?.cancel()
		loadJob = viewModelScope.launch {
			try {
				_uiState.update { it.copy(isLoading = true, error = null) }

				val selectedTab = _uiState.value.selectedTab
				val source = if (selectedTab == 0) {
					serviceDao.observeHistoryAsClient(CURRENT_USER_ID)
				} else {
					serviceDao.observeHistoryAsProvider(CURRENT_USER_ID)
				}

				source.collect { rows ->
					_uiState.update {
						it.copy(
							services = rows.map { row ->
								ServiceHistoryItem(
										serviceId = row.serviceId,
									title = row.title,
									provider = row.counterpartName,
									date = dateFormatter.format(Date(row.dateMillis)),
									price = "$${priceFormatter.format(row.totalPriceCop)} COP",
									imageUrl = row.imageUrl ?: DEFAULT_IMAGE_URL,
									rating = row.rating?.toFloat()
								)
							},
							isLoading = false
						)
					}
				}
			} catch (t: Throwable) {
				_uiState.update { it.copy(error = t.message ?: "Error desconocido", isLoading = false) }
			}
		}
	}

	private companion object {
		const val CURRENT_USER_ID = SampleData.SERVICE_LOCAL_USER_ID
		const val DEFAULT_IMAGE_URL = "https://images.unsplash.com/photo-1497366754035-f200968a6e72?w=300&h=300&fit=crop"
		val localeEsCo: Locale = Locale.forLanguageTag("es-CO")
		val dateFormatter = SimpleDateFormat("dd MMM yyyy", localeEsCo)
		val priceFormatter = NumberFormat.getNumberInstance(localeEsCo)
	}
}


