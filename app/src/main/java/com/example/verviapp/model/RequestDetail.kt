package com.example.verviapp.model

data class ClientSummary(
    val name: String,
    val rating: Float?,
    val location: String,
    val phone: String?,
    val avatarUrl: String?
)

data class ChatSummary(
    val lastMessage: String,
    val unreadCount: Int
)

data class RequestDetail(
    val id: String,
    val title: String,
    val date: String,
    val price: String,
    val description: String,
    val status: String,
    val roleLabel: String,
    val images: List<String> = emptyList(),
    val client: ClientSummary? = null,
    val chat: ChatSummary? = null
)

