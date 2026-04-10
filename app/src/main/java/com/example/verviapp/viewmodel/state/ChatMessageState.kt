package com.example.verviapp.model

data class ChatMessageState(
    val text: String,
    val time: String,
    val isUser: Boolean,
    val avatar: String? = null
)

