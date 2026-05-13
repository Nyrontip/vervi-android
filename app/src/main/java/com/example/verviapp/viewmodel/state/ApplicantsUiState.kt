package com.example.verviapp.viewmodel.state

data class ApplicantsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val applicants: List<ApplicantItem> = emptyList(),
    val requestTitle: String = ""
)

