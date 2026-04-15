package com.example.verviapp.viewmodel.state

data class RequestCancelUiState(
    val requestId: Int? = null,
    val selectedReason: String = "",
    val additionalDetails: String = "",
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val reasonOptions: List<String> = listOf(
        "No tengo disponibilidad",
        "No estoy de acuerdo con el precio",
        "El cliente no responde",
        "Otro motivo"
    )
)

