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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.verviapp.viewmodel.RequestDetailsViewModel
import com.example.verviapp.ui.components.organisms.ImageCarousel
import com.example.verviapp.ui.components.organisms.RequestDetailsCard
import com.example.verviapp.ui.components.organisms.RequestActionBar
import com.example.verviapp.ui.theme.VerviColors
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

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

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                vm.retry()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    val scrollState = rememberScrollState()
    var selectedCarouselIndex by remember { mutableIntStateOf(0) }

    // Safety check: ensure selected index is valid
    if (selectedCarouselIndex >= images.size && images.isNotEmpty()) {
        selectedCarouselIndex = 0
    }

    var showProviderDialog by remember { mutableStateOf(false) }
    if (showProviderDialog) {
        AlertDialog(
            onDismissRequest = { showProviderDialog = false },
            title = { Text("Modo prestador") },
            text = { Text("Debés activar el modo prestador en tu perfil para postularte a solicitudes.") },
            confirmButton = {
                TextButton(onClick = {
                    showProviderDialog = false
                    navController.navigate("editProfile")
                }) {
                    Text("Ir a mi perfil")
                }
            },
            dismissButton = {
                TextButton(onClick = { showProviderDialog = false }) {
                    Text("Ahora no")
                }
            }
        )
    }

    Scaffold(
        containerColor = VerviColors.BackgroundLight,
        bottomBar = if (uiState.isLoading) {
            { /* No mostrar action bar mientras carga */ }
        } else {
            {
                val hasProvider = uiState.provider != null
                RequestActionBar(
                    isOwner = uiState.isOwner,
                    hasProvider = hasProvider,
                    isClosed = uiState.isClosed,
                    onApplyClick = {
                        if (!vm.isLoggedIn()) {
                            navController.navigate("login")
                        } else if (!vm.isProvider()) {
                            showProviderDialog = true
                        } else {
                            requestId?.let { navController.navigate("service/apply/$it") }
                        }
                    },
                    onViewApplicationsClick = {
                        requestId?.let { navController.navigate("request/applications/$it") }
                    },
                    onCancelClick = {
                        requestId?.let { navController.navigate("request/cancel/$it") }
                    },
                    onConfirmClick = {
                        requestId?.let { navController.navigate("request/confirm/$it") }
                    }
                )
            }

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
            val personLabel = if (uiState.isOwner) "PROVEEDOR" else "PUBLICADO POR"
            val personName = if (uiState.isOwner) uiState.provider?.name else request?.client?.name
            val personRating = if (uiState.isOwner)
                uiState.provider?.rating?.let { "$it calificación" }
            else
                request?.client?.rating?.let { "$it calificación" }
            val personAvatar = if (uiState.isOwner) uiState.provider?.avatarUrl else request?.client?.avatarUrl
            val scope = rememberCoroutineScope()

            RequestDetailsCard(
                title = request?.title ?: "Cargando solicitud...",
                status = request?.status ?: "--",
                price = request?.price ?: "--",
                date = request?.date ?: "--",
                location = request?.location ?: request?.client?.location ?: "--",
                description = request?.description ?: "Sin descripción disponible.",
                publisherLabel = personLabel,
                publisherName = personName,
                publisherRating = personRating,
                publisherAvatarUrl = personAvatar,
                showChat = uiState.isOwner,
                onPublisherAvatarClick = {
                    val userId = if (uiState.isOwner) uiState.providerUserId else uiState.clientUserId
                    userId?.let { navController.navigate("profile?userId=$it") }
                },
                onPublisherChatClick = {
                    request?.id?.toIntOrNull()?.let { requestIdInt ->
                        uiState.providerUserId?.let { providerId ->
                            scope.launch {
                                val convId = vm.openChat(providerId, requestIdInt)
                                if (convId != null) {
                                    navController.navigate("chat/$convId")
                                }
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
