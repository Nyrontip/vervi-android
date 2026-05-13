package com.example.verviapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.remote.dto.ApplicationDto
import com.example.verviapp.data.repository.ApiResult
import com.example.verviapp.data.repository.RequestRepository
import com.example.verviapp.viewmodel.state.ApplicantItem
import com.example.verviapp.viewmodel.state.ApplicantsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ApplicantsViewModel @Inject constructor(
    private val repository: RequestRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val requestId: Int = savedStateHandle.get<Int>("requestId")
        ?: savedStateHandle.get<String>("requestId")?.toIntOrNull()
        ?: 0

    private val _uiState = MutableStateFlow(ApplicantsUiState())
    val uiState: StateFlow<ApplicantsUiState> = _uiState.asStateFlow()

    private val _acceptEvent = MutableSharedFlow<Unit>()
    val acceptEvent: SharedFlow<Unit> = _acceptEvent.asSharedFlow()

    private val currencyFormatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"))

    init {
        if (requestId > 0) load()
    }

    fun retry() {
        load()
    }

    fun accept(applicationId: Int) {
        viewModelScope.launch {
            when (repository.acceptApplication(applicationId)) {
                is ApiResult.Success -> _acceptEvent.emit(Unit)
                is ApiResult.Error -> { /* silencioso */ }
            }
        }
    }

    fun reject(applicationId: Int) {
        viewModelScope.launch {
            when (repository.rejectApplication(applicationId)) {
                is ApiResult.Success -> load()
                is ApiResult.Error -> { /* silencioso */ }
            }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val requestResult = repository.getRequestDetail(requestId)
            var reqTitle = ""
            if (requestResult is ApiResult.Success) {
                reqTitle = requestResult.data.title
            }

            when (val result = repository.getApplicationsByRequest(requestId)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        applicants = result.data.map { it.toItem() },
                        requestTitle = reqTitle,
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

    private fun ApplicationDto.toItem(): ApplicantItem {
        val price = proposedPriceCop?.let {
            "\$${currencyFormatter.format(it)} COP"
        } ?: "A convenir"

        return ApplicantItem(
            id = id,
            providerUserId = providerUserId,
            name = provider?.name ?: "Proveedor",
            avatarUrl = provider?.photoUrl?.takeIf(String::isNotBlank),
            rating = provider?.rating?.takeIf { it > 0f },
            proposedPrice = price,
            message = presentationMessage ?: "Sin mensaje",
            isAvailable = immediateAvailability,
            status = status
        )
    }
}
