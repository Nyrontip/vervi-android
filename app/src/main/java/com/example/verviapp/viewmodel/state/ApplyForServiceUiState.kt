package com.example.verviapp.viewmodel.state

data class ApplyForServiceUiState(
    val requestId: Int? = null,
    val requestTitle: String = "",
    val presentationMessage: String = "",
    val proposedPrice: String = "",
    val evidenceUri: String? = null,
    val immediateAvailability: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: String? = null
)

