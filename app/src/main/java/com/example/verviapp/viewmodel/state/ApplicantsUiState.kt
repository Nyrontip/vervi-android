package com.example.verviapp.viewmodel.state

data class ApplicantsUiState(
    val requestId: Int? = null,
    val requestTitle: String = "",
    val applicants: List<ApplicantItem> = emptyList(),
    val isLoading: Boolean = false,
    val isAcceptingApplicationId: Int? = null,
    val error: String? = null
)

