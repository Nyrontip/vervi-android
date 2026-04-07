package com.example.verviapp.Presentation.model

// Modelo de prestador de servicios
data class Provider(
    val id: String,
    val name: String,
    val specialty: String,
    val price: String,
    val rating: Float,
    val reviewCount: Int,
    val imageRes: Int        // referencia al drawable — luego será URL de API
)