package com.example.verviapp.model

data class RequestConfirmUiState(
    val requestId: Int? = null,
    val paymentReceiptConfirmed: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: String? = null
)

