package com.example.verviapp.model

data class ChatMessage(
    val text: String,
    val time: String,
    val isUser: Boolean,
    val avatar: String? = null
)

