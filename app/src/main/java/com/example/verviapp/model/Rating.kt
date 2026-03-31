package com.example.verviapp.model

data class RatingData(
    val providerName: String,
    val rating: Int,
    val comment: String,
    val imageUri: String? = null
)

