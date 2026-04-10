package com.example.verviapp.viewmodel.state

data class Service(
    val id: Int,
    val title: String,
    val location: String,
    val price: String,
    val applicationCount: Int,
    val isUrgent: Boolean,
    val imageUrl: String,
    val category: String
)
