package com.example.verviapp.viewmodel.state

data class ChatUiState(
    val messages: List<ChatMessageState> = emptyList(),
    val inputText: String = "",
    val showAttachments: Boolean = false,
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val error: String? = null,
    val otherParticipantName: String? = null,
    val otherParticipantAvatar: String? = null
)
