package com.example.verviapp.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Carousel indicator dot. Expands when active.
 */
@Composable
fun CarouselIndicator(
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(6.dp)
            .width(if (isActive) 22.dp else 6.dp)
            .clip(RoundedCornerShape(50))
            .background(
                if (isActive) Color.White
                else Color.White.copy(alpha = 0.5f)
            )
            .clickable { onClick() }
    )
}
