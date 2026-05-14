package com.example.verviapp.viewmodel.state

data class ApplicantItem(
    val id: Int,
    val providerUserId: Int,
    val name: String,
    val avatarUrl: String?,
    val rating: Float?,
    val proposedPrice: String,
    val message: String,
    val isAvailable: Boolean,
    val status: String
)

