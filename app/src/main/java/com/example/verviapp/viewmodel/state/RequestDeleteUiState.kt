package com.example.verviapp.viewmodel.state

data class RequestDeleteUiState(
    val requestId: Int? = null,
    val isSubmitting: Boolean = false,
    val error: String? = null
)
