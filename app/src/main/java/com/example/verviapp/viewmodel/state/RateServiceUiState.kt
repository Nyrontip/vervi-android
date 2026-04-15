package com.example.verviapp.viewmodel.state

data class RateServiceUiState(
    val serviceId: Int? = null,
    val counterpartUserId: Int? = null,
    val counterpartName: String = "",
    val counterpartAvatarUrl: String = "",
    val rating: Int = 4,
    val comment: String = "",
    val imageUri: String? = null,
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: String? = null
)

