package com.example.verviapp.viewmodel.state

data class RequestConfirmUiState(
    val requestId: Int? = null,
    val paymentReceiptConfirmed: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: String? = null
)

