package com.example.verviapp.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.verviapp.ui.theme.VerviAppTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavController
import coil.compose.AsyncImage


// ----------------------------
// DATA
// ----------------------------

data class Provider(
    val name: String,
    val avatarUrl: String
)

// ----------------------------
// COLORS
// ----------------------------

val Primary = Color(0xFF32619F)
val BackgroundLight = Color(0xFFF6F7F8)
val StarOrange = Color(0xFFFF9800)
val BorderGray = Color(0xFFE5E7EB)
val TextGray = Color(0xFF64748B)

// ----------------------------
// ATOMS
// ----------------------------

// BottomSheet Handle
@Composable
fun BottomSheetHandle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(6.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.LightGray)
        )
    }
}

// Avatar Image
@Composable
fun AvatarImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = modifier
            .size(56.dp)
            .clip(CircleShape)
    )
}

// Star Icon
@Composable
fun StarIcon(
    filled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Filled.Star,
        contentDescription = null,
        tint = if (filled) StarOrange else Color.LightGray,
        modifier = modifier
            .size(40.dp)
            .clickable { onClick() }
    )
}

// Primary Button
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Primary),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(text, fontSize = 16.sp)
    }
}

// Secondary Text Button
@Composable
fun SecondaryTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Text(text, color = TextGray)
    }
}

// Comment Input
@Composable
fun CommentInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {

        Text(
            text = "Comentario opcional",
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(fontSize = 14.sp),
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .border(
                    1.dp,
                    BorderGray,
                    RoundedCornerShape(12.dp)
                )
                .background(Color(0xFFF8FAFC))
                .padding(12.dp)
        )
    }
}

// Upload Evidence Button
@Composable
fun UploadEvidenceButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                2.dp,
                Color.LightGray,
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.AddAPhoto,
                contentDescription = null,
                tint = Primary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Adjuntar imagen de evidencia",
            fontSize = 14.sp
        )

        Text(
            "Sube una foto del servicio realizado (Opcional)",
            fontSize = 12.sp,
            color = TextGray
        )
    }
}

// ----------------------------
// MOLECULES
// ----------------------------

// Provider Header
@Composable
fun ProviderHeader(
    provider: Provider,
    modifier: Modifier = Modifier
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        AvatarImage(provider.avatarUrl)

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Calificar a ${provider.name}",
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "¿Cómo fue tu experiencia con el servicio?",
            fontSize = 14.sp,
            color = TextGray
        )
    }
}

// Star Rating Selector
@Composable
fun StarRatingSelector(
    rating: Int,
    onRatingSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {

        for (i in 1..5) {

            StarIcon(
                filled = i <= rating,
                onClick = { onRatingSelected(i) },
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

// ----------------------------
// ORGANISM
// ----------------------------

@Composable
fun RatingBottomSheet(
    navController: NavController,
    provider: Provider,
    modifier: Modifier = Modifier
) {

    var rating by remember { mutableStateOf(4) }
    var comment by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStart = 28.dp,
                    topEnd = 28.dp
                )
            )
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {

        BottomSheetHandle()

        Spacer(modifier = Modifier.height(16.dp))

        ProviderHeader(provider)

        Spacer(modifier = Modifier.height(24.dp))

        StarRatingSelector(
            rating = rating,
            onRatingSelected = { rating = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        CommentInput(
            value = comment,
            onValueChange = { comment = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        UploadEvidenceButton(
            onClick = { }
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Enviar Calificación",
            onClick = { }
        )

        SecondaryTextButton(
            text = "Omitir por ahora",
            onClick = { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// ----------------------------
// SCREEN / TEMPLATE
// ----------------------------

@Composable
fun RateServiceScreen(navController: NavController) {

    val provider = Provider(
        name = "Juan Pérez",
        avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuANtCUGY5nCwclrXbDrVX1FXXyOT6BJpuuUGLpmbLbk42YEKjQAy_ymEJ5jfZdYfq1-FYGyJIIudW5w7gyrr79NnCrNbTCmNw39HgpwYsd_ZjQaJc8JJULFyYUFaL3mixnNaS1wVy3cNywMdeb3zihM5nHn3-XG4FU5Xaem20N0MOE3oHUXHxunqolzDdGPGVpIDkHvI2yz-KCo9GCmy-dKbxw5v6GHcZX53nbvfpn8TQGvg07GZd8aw8Jk8QO4LEKPfZBjVSgLhMdh"
    )

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        containerColor = BackgroundLight
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
                    .background(Color.Black.copy(alpha = 0.4f))
            )

            // Bottom Sheet
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                RatingBottomSheet(navController,provider)
            }
        }
    }
}

