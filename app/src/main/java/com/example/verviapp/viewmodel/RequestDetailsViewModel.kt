package com.example.verviapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.dao.RequestDetailsDao
import com.example.verviapp.data.repository.SampleData
import com.example.verviapp.viewmodel.state.ChatSummary
import com.example.verviapp.viewmodel.state.ClientSummary
import com.example.verviapp.viewmodel.state.RequestDetailItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

data class RequestDetailsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val request: RequestDetailItem? = null
)

@HiltViewModel
class RequestDetailsViewModel @Inject constructor(
    private val requestDetailsDao: RequestDetailsDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val requestId: Int = savedStateHandle.get<String>("requestId")
        ?.toIntOrNull()
        ?: SampleData.REQUEST_DETAILS_REQUEST_ID

    private val _uiState = MutableStateFlow(RequestDetailsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val request = requestDetailsDao.getRequestById(requestId)
                if (request == null) {
                    _uiState.value = _uiState.value.copy(
                        error = "No se encontro la solicitud $requestId",
                        isLoading = false
                    )
                    return@launch
                }

                val attachments = requestDetailsDao.getAttachmentsByRequestId(request.id)
                val clientEntity = request.clientUserId?.let { requestDetailsDao.getUserById(it) }
                val conversation = requestDetailsDao.getConversationByRequestId(request.id)

                val data = RequestDetailItem(
                    id = request.id.toString(),
                    title = request.title,
                    date = request.date,
                    price = formatBudget(request.budgetCop),
                    location = request.location,
                    description = request.description,
                    status = request.status,
                    roleLabel = "Como: Prestador",
                    images = attachments.map { it.uri }.ifEmpty {
                        listOfNotNull(request.imageUrl.takeIf(String::isNotBlank))
                    },
                    client = clientEntity?.let {
                        ClientSummary(
                            name = it.name,
                            rating = it.rating.takeIf { rating -> rating > 0f },
                            location = it.location,
                            phone = null,
                            avatarUrl = it.photoUrl.takeIf(String::isNotBlank)
                        )
                    },
                    chat = conversation?.lastMessagePreview?.let {
                        ChatSummary(lastMessage = it, unreadCount = 0)
                    }
                )

                _uiState.value = _uiState.value.copy(request = data, isLoading = false)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(error = t.message ?: "Error desconocido", isLoading = false)
            }
        }
    }

    private fun formatBudget(budgetCop: Long?): String {
        if (budgetCop == null) return "Sin presupuesto"
        val formatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"))
        return "$${formatter.format(budgetCop)} COP"
    }
}


