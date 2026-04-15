package com.example.verviapp.viewmodel.state

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class RequestItem(
    val id: Int,
    val status: String,
    val statusColor: Color,
    val title: String,
    val date: String,
    val applications: String,
    val imageUrl: String,
    val buttonText: String,
    val secondaryIcon: ImageVector
)

