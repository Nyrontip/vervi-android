package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.repository.ApiResult
import com.example.verviapp.data.repository.ChatRepository
import com.example.verviapp.viewmodel.state.ChatMessageState
import com.example.verviapp.viewmodel.state.ChatUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var conversationId: Int = 0
    private var tempIdCounter = 0

    fun init(convId: Int) {
        if (conversationId == convId) return
        conversationId = convId
        loadConversation()
    }

    fun onInputChange(text: String) {
        _uiState.value = _uiState.value.copy(inputText = text)
    }

    fun sendMessage() {
        val text = _uiState.value.inputText.trim()
        if (text.isEmpty() || conversationId == 0) return

        // Optimistic: agregar mensaje inmediatamente
        val tempId = tempIdCounter--
        val now = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val currentUserId = repository.getLoggedInUserId()

        val optimistic = ChatMessageState(
            id = tempId,
            text = text,
            time = now,
            isUser = true,
            avatar = null
        )
        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + optimistic,
            inputText = "",
            isSending = false,
            showAttachments = false,
            error = null
        )

        // Llamada real al backend en background
        viewModelScope.launch {
            when (repository.sendMessage(conversationId, text)) {
                is ApiResult.Success -> loadMessages()
                is ApiResult.Error -> { /* el mensaje temporal se reemplaza al recargar */ }
            }
        }
    }

    fun retry() {
        loadMessages()
    }

    fun toggleAttachments() {
        _uiState.value = _uiState.value.copy(showAttachments = !_uiState.value.showAttachments)
    }

    private fun loadConversation() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = repository.getConversation(conversationId)) {
                is ApiResult.Success -> {
                    val other = repository.otherParticipant(result.data)
                    _uiState.value = _uiState.value.copy(
                        otherParticipantName = other?.name,
                        otherParticipantAvatar = other?.photoUrl?.takeIf(String::isNotBlank)
                    )
                    loadMessages()
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

    private fun loadMessages() {
        if (conversationId == 0) return

        viewModelScope.launch {
            when (val result = repository.getMessages(conversationId)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        messages = result.data,
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
}
