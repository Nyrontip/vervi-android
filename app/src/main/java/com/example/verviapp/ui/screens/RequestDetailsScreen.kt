package com.example.verviapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.verviapp.viewmodel.RequestDetailsViewModel
import coil.compose.AsyncImage
import com.example.verviapp.ui.components.VerviButton
import com.example.verviapp.ui.theme.VerviColors

/**
 * Detail screen for a **request / solicitud** (open job): hero, budget, apply flow.
 * For completed **service** detail (prestador, evidencia, calificar), use [ServiceDetailsScreen].
 */
private val headerImages = listOf(
    "https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=1200&h=900&fit=crop",
    "https://images.unsplash.com/photo-1581578731548-c64695cc6952?w=1200&h=900&fit=crop",
    "https://images.unsplash.com/photo-1621905251918-48416bd8575a?w=1200&h=900&fit=crop"
)

@Composable
fun RequestDetailsScreen(navController: NavController, vm: RequestDetailsViewModel = viewModel()) {
    val scrollState = rememberScrollState()
    var selectedCarouselIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = VerviColors.BackgroundLight,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .navigationBarsPadding()
            ) {
                HorizontalDivider(color = VerviColors.BorderGray)
                VerviButton(
                    text = "Postularse",
                    onClick = { navController.navigate("service/apply") },
                    color = VerviColors.OrangeSecondary,
                    icon = Icons.AutoMirrored.Filled.Send,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    height = 54.dp
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(305.dp)
            ) {
                AsyncImage(
                    model = headerImages[selectedCarouselIndex],
                    contentDescription = "Imagen solicitud",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp)
                        .size(34.dp)
                        .align(Alignment.TopStart)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.28f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    headerImages.indices.forEach { index ->
                        Box(
                            modifier = Modifier
                                .height(6.dp)
                                .width(if (index == selectedCarouselIndex) 22.dp else 6.dp)
                                .clip(RoundedCornerShape(50))
                                .background(
                                    if (index == selectedCarouselIndex) Color.White
                                    else Color.White.copy(alpha = 0.5f)
                                )
                                .clickable { selectedCarouselIndex = index }
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
                colors = CardDefaults.cardColors(containerColor = VerviColors.CardBackground)
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Reparación de tubería",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = VerviColors.TextDark
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFFE3F8EA))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "ABIERTO",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2A8B4B)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Today,
                            contentDescription = null,
                            tint = VerviColors.Primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$50.000 COP",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = VerviColors.TextDark
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Presupuesto sugerido",
                            fontSize = 14.sp,
                            color = VerviColors.TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        RequestInfoPill(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Outlined.Today,
                            label = "FECHA",
                            value = "Hoy, 14:00"
                        )
                        RequestInfoPill(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Outlined.LocationOn,
                            label = "UBICACIÓN",
                            value = "Bogotá, DC"
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Descripción",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerviColors.TextDark
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Se requiere reparación urgente de una fuga en la tubería principal de la cocina. El agua se está filtrando por debajo del mueble. Necesito a alguien con experiencia previa en plomería residencial. Cuento con algunas herramientas pero prefiero que traigan las suyas.",
                        fontSize = 16.sp,
                        lineHeight = 25.sp,
                        color = Color(0xFF4B5563)
                    )

                    Spacer(modifier = Modifier.height(26.dp))

                    Text(
                        text = "PUBLICADO POR",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9AA2AF),
                        letterSpacing = 1.2.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    RequestPublisherCard(
                        name = "Carlos J. Martinez",
                        ratingLine = "4.8 (12 servicios)",
                        avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=300&h=300&fit=crop",
                        onChatClick = { navController.navigate("chat") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun RequestInfoPill(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF3F5F8))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = VerviColors.Primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF697384)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = VerviColors.TextDark
        )
    }
}

@Composable
private fun RequestPublisherCard(
    name: String,
    ratingLine: String,
    avatarUrl: String,
    onChatClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, VerviColors.BorderGray, RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            AsyncImage(
                model = avatarUrl,
                contentDescription = "Publicador",
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(Color(0xFF22C55E))
                    .border(2.dp, Color.White, CircleShape)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = VerviColors.TextDark
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = ratingLine,
                color = VerviColors.TextSecondary,
                fontSize = 14.sp
            )
        }

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color(0xFFF0F4F8))
                .clickable { onChatClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Chat,
                contentDescription = "Chat",
                tint = VerviColors.Primary
            )
        }
    }
}

