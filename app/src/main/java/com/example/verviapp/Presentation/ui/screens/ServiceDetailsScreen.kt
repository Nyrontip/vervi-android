package com.example.verviapp.Presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.verviapp.ui.components.VerviButton
import com.example.verviapp.ui.components.VerviTopBar
import com.example.verviapp.ui.theme.VerviColors

/** Service detail screen — completed job (mockup background). */
private val ScreenBackground = Color(0xFFF8F7F5)

private val roleProviderBadgeBg = Color(0xFFFFE8D6)
private val statusCompletedBg = Color(0xFFE6F4EA)
private val statusCompletedText = Color(0xFF1E8E3E)

private const val serviceTitleLabel = "Reparación Aire Acondicionado"
private const val servicePriceDisplay = "$125.000"
private const val workSummaryBody =
    "Se realizó la revisión técnica completa del sistema central. Se identificó fuga en el serpentín, se procedió a sellado y recarga de gas refrigerante R-410A. Limpieza profunda de filtros y drenaje incluida."

private val evidenceImageUrls = listOf(
    "https://images.unsplash.com/photo-1621905251918-48416bd8575a?w=600&h=800&fit=crop",
    "https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=400&h=500&fit=crop",
    "https://images.unsplash.com/photo-1581578731548-c64695cc6952?w=400&h=400&fit=crop",
    "https://images.unsplash.com/photo-1621905251189-08b45d6a269e?w=400&h=400&fit=crop"
)

private const val clientAvatarUrl =
    "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&h=200&fit=crop"

@Composable
fun ServiceDetailsScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = ScreenBackground,
        topBar = {
            VerviTopBar(
                title = "Detalles del Servicio",
                onBack = { navController.popBackStack() },
                barContainerColor = ScreenBackground,
                actions = {
                    IconButton(onClick = { /* TODO: share */ }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartir",
                            tint = VerviColors.TextDark
                        )
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ScreenBackground)
                    .navigationBarsPadding()
            ) {
                HorizontalDivider(color = VerviColors.BorderGray)
                VerviButton(
                    text = "Calificar Servicio",
                    onClick = { navController.navigate("service/rate") },
                    color = VerviColors.OrangeSecondary,
                    icon = Icons.Default.Star,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    height = 54.dp
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ScreenBackground)
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                RoleBadge(text = "Como: Prestador")
                StatusPill(text = "Completado", background = statusCompletedBg, content = statusCompletedText)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = serviceTitleLabel,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerviColors.TextDark,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = servicePriceDisplay,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerviColors.OrangeSecondary
                    )
                    Text(
                        text = "COP TOTAL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = VerviColors.TextSecondary,
                        letterSpacing = 0.8.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.CalendarToday,
                    contentDescription = null,
                    tint = VerviColors.TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "14 de Octubre, 2023 • 10:30 AM",
                    fontSize = 14.sp,
                    color = VerviColors.TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle(text = "Resumen del Trabajo")
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = workSummaryBody,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 15.sp,
                    lineHeight = 23.sp,
                    color = Color(0xFF4B5563)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle(text = "Evidencia del Servicio")
            Spacer(modifier = Modifier.height(10.dp))
            EvidenceGrid(imageUrls = evidenceImageUrls)

            Spacer(modifier = Modifier.height(22.dp))

            ChatSummaryCard(onOpenChat = { navController.navigate("chat") })

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle(text = "Cliente")
            Spacer(modifier = Modifier.height(8.dp))
            ClientCard(
                clientName = "Mariana Restrepo",
                ratingText = "4.9",
                locationText = "Medellín, Antioquia",
                avatarUrl = clientAvatarUrl,
                onCallClick = { /* TODO: dial */ }
            )

            Spacer(modifier = Modifier.height(88.dp))
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = VerviColors.TextDark
    )
}

@Composable
private fun RoleBadge(text: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(roleProviderBadgeBg)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            tint = VerviColors.OrangeSecondary,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = VerviColors.TextDark
        )
    }
}

@Composable
private fun StatusPill(text: String, background: Color, content: Color) {
    Text(
        text = text,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(background)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = content
    )
}

@Composable
private fun EvidenceGrid(imageUrls: List<String>) {
    val main = imageUrls.getOrNull(0).orEmpty()
    val mid = imageUrls.getOrNull(1).orEmpty()
    val topRight = imageUrls.getOrNull(2).orEmpty()
    val bottomRight = imageUrls.getOrNull(3).orEmpty()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            model = main,
            contentDescription = "Evidencia 1",
            modifier = Modifier
                .weight(0.42f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.weight(0.58f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    model = mid,
                    contentDescription = "Evidencia 2",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    AsyncImage(
                        model = topRight,
                        contentDescription = "Evidencia 3",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black.copy(alpha = 0.45f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+2",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }
            AsyncImage(
                model = bottomRight,
                contentDescription = "Evidencia 4",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun ChatSummaryCard(onOpenChat: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenChat() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(VerviColors.OrangeSecondary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Chat,
                    contentDescription = null,
                    tint = VerviColors.OrangeSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Resumen de Chat",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = VerviColors.TextDark
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Ver conversación con el cliente",
                    fontSize = 13.sp,
                    color = VerviColors.TextSecondary
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = VerviColors.TextGray
            )
        }
    }
}

@Composable
private fun ClientCard(
    clientName: String,
    ratingText: String,
    locationText: String,
    avatarUrl: String,
    onCallClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = "Cliente",
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = clientName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = VerviColors.TextDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = null,
                        tint = VerviColors.StarFilled,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = ratingText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = VerviColors.TextDark
                    )
                    Text(
                        text = " • $locationText",
                        fontSize = 14.sp,
                        color = VerviColors.TextSecondary
                    )
                }
            }
            IconButton(onClick = onCallClick) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(VerviColors.OrangeSecondary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Phone,
                        contentDescription = "Llamar",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}
