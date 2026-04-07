package com.example.verviapp.Presentation.model

data class User(
    val id: String = "1",
    val name: String = "",
    val email: String = "",
    val bio: String = "",
    val location: String = "",
    val photoUrl: String = "",
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val suggestedPrice: String = "",
    val categories: List<String> = emptyList(),
    val isProvider: Boolean = false,
    val projectCount: Int = 0,
    val requestCount: Int = 0
)