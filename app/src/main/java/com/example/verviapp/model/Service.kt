package com.example.verviapp.model

data class Service(
    val id: String,
    val title: String,
    val location: String,
    val price: String,
    val applicationCount: Int,
    val isUrgent: Boolean,
    val imageRes: Int,       // referencia al drawable — luego será URL de API
    val category: String
)