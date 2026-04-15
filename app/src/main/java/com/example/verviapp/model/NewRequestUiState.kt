package com.example.verviapp.model

import android.net.Uri

data class NewRequestUiState(
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val budget: String = "",
    val dateText: String = "",
    val dateMillis: Long? = null,
    val attachments: List<Uri?> = listOf(null, null, null),
    val categoryOptions: List<String> = emptyList(),
    val isLoadingCategories: Boolean = false,
    val categoriesError: String? = null,
    val isSubmitting: Boolean = false,
    val error: String? = null
)

