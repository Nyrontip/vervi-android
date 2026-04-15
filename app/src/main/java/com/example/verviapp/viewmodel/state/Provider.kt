package com.example.verviapp.viewmodel.state

// Modelo de prestador de servicios
data class Provider(
    val id: Int,
    val name: String,
    val specialty: String,
    val price: String,
    val rating: Float,
    val reviewCount: Int,
    val imageUrl: String
)
