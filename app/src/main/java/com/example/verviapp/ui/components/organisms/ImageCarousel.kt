package com.example.verviapp.ui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import com.example.verviapp.ui.components.atoms.BackButton
import com.example.verviapp.ui.components.atoms.CarouselIndicator
import com.example.verviapp.ui.components.atoms.ContentPlaceholder
import com.example.verviapp.ui.theme.VerviColors

/**
 * Hero image carousel with back button and indicators.
 */
@Composable
fun ImageCarousel(
    images: List<String>,
    selectedIndex: Int,
    onImageSelected: (Int) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: androidx.compose.ui.unit.Dp = 305.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        // Image or placeholder
        if (images.isNotEmpty()) {
            AsyncImage(
                model = images[selectedIndex],
                contentDescription = "Imagen solicitud",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            ContentPlaceholder(
                modifier = Modifier.fillMaxSize(),
                icon = Icons.Outlined.Image
            )
        }

        // Back button (top-left)
        BackButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp)
                .align(Alignment.TopStart)
        )

        // Carousel indicators (bottom-center)
        if (images.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                images.indices.forEach { index ->
                    CarouselIndicator(
                        isActive = index == selectedIndex,
                        onClick = { onImageSelected(index) }
                    )
                }
            }
        }
    }
}
