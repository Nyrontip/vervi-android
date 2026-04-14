package com.example.verviapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.verviapp.viewmodel.RateServiceViewModel
import com.example.verviapp.viewmodel.RateServiceEvent
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.verviapp.ui.components.*
import com.example.verviapp.ui.theme.VerviColors
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

// ----------------------------
// 🔹 NUEVOS COMPONENTES REUTILIZABLES
// ----------------------------

// Avatar global
@Composable
fun VerviAvatar(
    imageUrl: String,
    size: Dp = 64.dp
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
    )
}

// Rating interactivo
@Composable
fun VerviInteractiveRating(
    rating: Int,
    onRatingSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (i <= rating)
                    VerviColors.StarFilled
                else
                    VerviColors.StarEmpty,
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onRatingSelected(i) }
                    .padding(horizontal = 4.dp)
            )
        }
    }
}

// ----------------------------
// 🔹 UI PRINCIPAL
// ----------------------------

@Composable
fun RatingBottomSheet(
    navController: NavController,
    viewModel: RateServiceViewModel,
    onPickImage: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(44.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(50))
                    .background(VerviColors.BorderGray)
            )
        }

        Spacer(Modifier.height(16.dp))

        // Header
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            VerviAvatar(uiState.counterpartAvatarUrl)

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Calificar a ${uiState.counterpartName}",
                fontSize = 20.sp,
                color = VerviColors.TextPrimary
            )

            Text(
                text = "¿Cómo fue tu experiencia con el servicio?",
                fontSize = 13.sp,
                color = VerviColors.TextSecondary
            )
        }

        Spacer(Modifier.height(24.dp))

        // Rating
        VerviInteractiveRating(
            rating = uiState.rating,
            onRatingSelected = { viewModel.onRatingSelected(it) }
        )

        Spacer(Modifier.height(24.dp))

        // Comentario
        VerviTextArea(
            label = "Comentario opcional",
            value = uiState.comment,
            onValueChange = { viewModel.onCommentChange(it) },
            placeholder = "Escribe tu experiencia..."
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = "EVIDENCIA (OPCIONAL)",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = VerviColors.TextSecondary,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(6.dp))
        AttachmentSlot(
            uri = uiState.imageUri?.let(Uri::parse),
            onAddClick = onPickImage,
            onRemoveClick = { viewModel.onAttachImage(null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        // Botón principal
        VerviButton(
            text = if (uiState.isSubmitting) "Enviando..." else "Enviar calificación",
            onClick = {
                if (!uiState.isSubmitting && !uiState.isLoading) {
                    viewModel.submitRating()
                }
            },
        )

        // Omitir
        TextButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Omitir por ahora",
                color = VerviColors.TextSecondary
            )
        }

        Spacer(Modifier.height(12.dp))
    }
}

// ----------------------------
// 🔹 SCREEN
// ----------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateServiceScreen(navController: NavController, serviceId: Int) {
    val viewModel: RateServiceViewModel = hiltViewModel()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(serviceId) {
        viewModel.load(serviceId)
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            if (event is RateServiceEvent.Submitted) {
                navController.popBackStack()
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onAttachImage(uri?.toString())
    }

    ModalBottomSheet(
        onDismissRequest = { navController.popBackStack() },
        sheetState = sheetState,
        containerColor = VerviColors.BottomSheetBackground,
        scrimColor = VerviColors.Overlay,
        sheetMaxWidth = Dp.Unspecified
    ) {
        RatingBottomSheet(
            navController = navController,
            viewModel = viewModel,
            onPickImage = { launcher.launch("image/*") }
        )
    }
}


