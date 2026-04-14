package com.example.verviapp.viewmodel.state

data class ChatMessageState(
    val text: String,
    val time: String,
    val isUser: Boolean,
    val avatar: String? = null
)

