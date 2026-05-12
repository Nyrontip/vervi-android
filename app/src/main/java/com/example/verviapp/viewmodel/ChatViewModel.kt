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
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var conversationId: Int = 0

    fun init(convId: Int) {
        if (conversationId == convId) return
        conversationId = convId
        loadMessages()
    }

    fun onInputChange(text: String) {
        _uiState.value = _uiState.value.copy(inputText = text)
    }

    fun sendMessage() {
        val text = _uiState.value.inputText.trim()
        if (text.isEmpty() || conversationId == 0) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSending = true, error = null)
            when (repository.sendMessage(conversationId, text)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        inputText = "",
                        isSending = false,
                        showAttachments = false
                    )
                    loadMessages()
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isSending = false,
                        error = "No fue posible enviar el mensaje"
                    )
                }
            }
        }
    }

    fun retry() {
        loadMessages()
    }

    fun toggleAttachments() {
        _uiState.value = _uiState.value.copy(showAttachments = !_uiState.value.showAttachments)
    }

    private fun loadMessages() {
        if (conversationId == 0) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
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
