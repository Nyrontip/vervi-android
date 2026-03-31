package com.example.verviapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.verviapp.viewmodel.RateServiceViewModel
import com.example.verviapp.repository.RatingsRepositoryImpl
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.verviapp.ui.components.*
import com.example.verviapp.ui.theme.VerviColors
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

// ----------------------------
// DATA
// ----------------------------

data class Provider(
    val name: String,
    val avatarUrl: String
)

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

// Upload Card reutilizable
@Composable
fun VerviUploadCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.5.dp,
                VerviColors.BorderGray,
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(VerviColors.Primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AddAPhoto,
                contentDescription = null,
                tint = VerviColors.Primary
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = title,
            fontSize = 14.sp,
            color = VerviColors.TextPrimary
        )

        Text(
            text = subtitle,
            fontSize = 12.sp,
            color = VerviColors.TextSecondary
        )
    }
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
    provider: Provider,
    launcher: ManagedActivityResultLauncher<String, Uri?>,
    viewModel: RateServiceViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .background(VerviColors.BottomSheetBackground)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {

        // Handle
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

            VerviAvatar(provider.avatarUrl)

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Calificar a ${provider.name}",
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

        // Upload
        VerviUploadCard(
            title = "Adjuntar imagen de evidencia",
            subtitle = "Sube una foto del servicio realizado (Opcional)",
            onClick = {
                launcher.launch("image/*")
            }
        )

        Spacer(Modifier.height(24.dp))

        // Botón principal
        VerviButton(
            text = if (uiState.isSubmitting) "Enviando..." else "Enviar calificación",
            onClick = {
                viewModel.submitRating(provider.name)
                navController.popBackStack()
            }
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

@Composable
fun RateServiceScreen(navController: NavController) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // actualizar el viewModel con la Uri como string
        uri?.let { _uri ->
            // veremos inyectado el viewModel más abajo
        }
    }

    val viewModel: RateServiceViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RateServiceViewModel(RatingsRepositoryImpl()) as T
            }
        }
    )

    val provider = Provider(
        name = "Juan Pérez",
        avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuANtCUGY5nCwclrXbDrVX1FXXyOT6BJpuuUGLpmbLbk42YEKjQAy_ymEJ5jfZdYfq1-FYGyJIIudW5w7gyrr79NnCrNbTCmNw39HgpwYsd_ZjQaJc8JJULFyYUFaL3mixnNaS1wVy3cNywMdeb3zihM5nHn3-XG4FU5Xaem20N0MOE3oHUXHxunqolzDdGPGVpIDkHvI2yz-KCo9GCmy-dKbxw5v6GHcZX53nbvfpn8TQGvg07GZd8aw8Jk8QO4LEKPfZBjVSgLhMdh"
    )

    Scaffold(
        containerColor = VerviColors.BackgroundLight
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(VerviColors.Overlay)
            )

            // BottomSheet
                Box(
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    RatingBottomSheet(navController, provider, launcher, viewModel)
                }
        }
    }
}