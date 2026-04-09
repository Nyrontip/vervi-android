package com.example.verviapp.viewmodel.state

data class ServiceHistoryItem(
    val title: String,
    val provider: String,
    val date: String,
    val price: String,
    val imageUrl: String,
    val rating: Float?,
)
