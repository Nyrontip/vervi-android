package com.example.verviapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.verviapp.viewmodel.RequestDetailsViewModel
import com.example.verviapp.ui.components.organisms.ImageCarousel
import com.example.verviapp.ui.components.organisms.RequestDetailsCard
import com.example.verviapp.ui.components.organisms.RequestActionBar
import com.example.verviapp.ui.theme.VerviColors

/**
 * Detail screen for a **request / solicitud** (open job): hero, budget, apply flow.
 * For completed **service** detail (prestador, evidencia, calificar), use [ServiceDetailsScreen].
 *
 * Architecture: Uses atomic design pattern with atoms, molecules, and organisms.
 */
@Composable
fun RequestDetailsScreen(
    navController: NavController,
    vm: RequestDetailsViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    val request = uiState.request
    val requestId = request?.id?.toIntOrNull()
    val images = request?.images.orEmpty()
    val scrollState = rememberScrollState()
    var selectedCarouselIndex by remember { mutableIntStateOf(0) }

    // Safety check: ensure selected index is valid
    if (selectedCarouselIndex >= images.size && images.isNotEmpty()) {
        selectedCarouselIndex = 0
    }

    Scaffold(
        containerColor = VerviColors.BackgroundLight,
        bottomBar = {
            RequestActionBar(
                isOwner = uiState.isOwner,
                onApplyClick = {
                    requestId?.let { navController.navigate("service/apply/$it") }
                },
                onViewApplicationsClick = {
                    requestId?.let { navController.navigate("request/applications/$it") }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VerviColors.BackgroundLight)
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {
            // Hero carousel with back button and indicators
            ImageCarousel(
                images = images,
                selectedIndex = selectedCarouselIndex,
                onImageSelected = { selectedCarouselIndex = it },
                onBackClick = { navController.popBackStack() }
            )

            // Main content card
            RequestDetailsCard(
                title = request?.title ?: "Cargando solicitud...",
                status = request?.status ?: "--",
                price = request?.price ?: "--",
                date = request?.date ?: "--",
                location = request?.location ?: request?.client?.location ?: "--",
                description = request?.description ?: "Sin descripción disponible.",
                publisherName = request?.client?.name,
                publisherRating = request?.client?.rating?.let { "$it calificación" },
                publisherAvatarUrl = request?.client?.avatarUrl,
                isOwner = uiState.isOwner,
                onPublisherChatClick = { navController.navigate("chat") }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
