package com.example.verviapp.viewmodel.state

data class RequestDetailItem(
    val id: String,
    val title: String,
    val date: String,
    val price: String,
    val location: String,
    val description: String,
    val status: String,
    val roleLabel: String,
    val images: List<String> = emptyList(),
    val client: ClientSummary? = null,
    val chat: ChatSummary? = null
)

