package com.example.verviapp.viewmodel.state

data class ApplicantItem(
    val applicationId: Int,
    val providerUserId: Int,
    val name: String,
    val price: String,
    val rating: Float,
    val reviews: Int,
    val message: String,
    val imageUrl: String,
    val status: String,
    val isImmediateAvailability: Boolean
)

