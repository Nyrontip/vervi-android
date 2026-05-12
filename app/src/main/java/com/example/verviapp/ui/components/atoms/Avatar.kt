package com.example.verviapp.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.verviapp.ui.theme.VerviColors

/**
 * Generic placeholder for missing content (image or avatar).
 */
@Composable
fun ContentPlaceholder(
    modifier: Modifier = Modifier,
    isCircular: Boolean = false,
    icon: androidx.compose.ui.graphics.vector.ImageVector = Icons.Outlined.Image
) {
    val shape = if (isCircular) CircleShape else RoundedCornerShape(0.dp)
    Box(
        modifier = modifier
            .clip(shape)
            .background(VerviColors.BorderGray.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = VerviColors.TextGray,
            modifier = Modifier.size(if (isCircular) 28.dp else 72.dp)
        )
    }
}

/**
 * Avatar image with fallback placeholder.
 */
@Composable
fun Avatar(
    imageUrl: String?,
    contentDescription: String = "Avatar",
    modifier: Modifier = Modifier.size(54.dp),
    isOnline: Boolean = false
) {
    Box {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = contentDescription,
                modifier = modifier.clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            ContentPlaceholder(
                modifier = modifier.clip(CircleShape),
                isCircular = true,
                icon = Icons.Outlined.Person
            )
        }

        // Online indicator
        if (isOnline) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(Color(0xFF22C55E))
                    .border(2.dp, Color.White, CircleShape)
            )
        }
    }
}
