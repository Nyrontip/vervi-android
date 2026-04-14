package com.example.verviapp.viewmodel.state

// Representa el estado de la UI del chat
data class ChatUiState(
    val messages: List<ChatMessageState> = emptyList(),
    val inputText: String = "",
    val showAttachments: Boolean = false,
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val error: String? = null
)

