package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.model.ChatMessage
import com.example.verviapp.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatUiState(
	val messages: List<ChatMessage> = emptyList(),
	val inputText: String = "",
	val showAttachments: Boolean = false,
	val isSending: Boolean = false,
	val error: String? = null
)

class ChatViewModel(
	private val repository: ChatRepository
) : ViewModel() {

	private val _uiState = MutableStateFlow(ChatUiState())
	val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

	init {
		loadHistory()
	}

	fun loadHistory() {
		viewModelScope.launch {
			_uiState.value = _uiState.value.copy(messages = repository.getHistory())
		}
	}

	fun onInputChange(text: String) {
		_uiState.value = _uiState.value.copy(inputText = text)
	}

	fun toggleAttachments() {
		_uiState.value = _uiState.value.copy(showAttachments = !_uiState.value.showAttachments)
	}

	fun sendMessage() {
		val text = _uiState.value.inputText.trim()
		if (text.isEmpty()) return

		viewModelScope.launch {
			_uiState.value = _uiState.value.copy(isSending = true)
			val msg = ChatMessage(text, "Ahora", true)
			repository.send(msg)
			_uiState.value = _uiState.value.copy(
				messages = repository.getHistory(),
				inputText = "",
				isSending = false
			)
		}
	}

	fun refresh() {
		loadHistory()
	}
}
