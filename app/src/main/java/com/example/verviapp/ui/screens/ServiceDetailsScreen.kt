package com.example.verviapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.verviapp.ui.components.*
import com.example.verviapp.ui.components.molecules.PublisherCard
import com.example.verviapp.ui.theme.VerviColors
import com.example.verviapp.viewmodel.ServiceDetailsViewModel
import kotlinx.coroutines.launch

private val ScreenBackground = Color(0xFFF8F7F5)

@Composable
fun ServiceDetailsScreen(
    navController: NavController,
    serviceId: Int,
    vm: ServiceDetailsViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    val detail = uiState.detail
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(serviceId) {
        vm.load(serviceId)
    }

    Scaffold(
        containerColor = ScreenBackground,
        topBar = {
            VerviTopBar(
                title = "Detalle del Servicio",
                onBack = { navController.popBackStack() },
                barContainerColor = ScreenBackground
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
                    text = "Ver conversación",
                    onClick = {
                        val counterpartId = uiState.counterpartUserId
                        if (counterpartId != null) {
                            scope.launch {
                                val convId = vm.openChat(counterpartId, null)
                                if (convId != null) {
                                    navController.navigate("chat/$convId")
                                }
                            }
                        }
                    },
                    color = VerviColors.Blue,
                    icon = Icons.Outlined.Chat,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    height = 54.dp
                )
            }
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = VerviColors.Blue)
                }
            }

            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = VerviColors.CancelRed,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = uiState.error!!,
                            fontSize = 14.sp,
                            color = VerviColors.TextDark,
                            modifier = Modifier.padding(horizontal = 32.dp),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(16.dp))
                        VerviSmallButton(
                            text = "Reintentar",
                            color = VerviColors.Blue,
                            onClick = { vm.retry() },
                            modifier = Modifier.widthIn(min = 180.dp)
                        )
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(ScreenBackground)
                        .padding(innerPadding)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(Modifier.height(8.dp))

                    // Status badge with proper colors
                    detail?.statusText?.let { status ->
                        val statusColor = when (status) {
                            "Programado" -> Color(0xFFF59E0B)
                            "En curso" -> Color(0xFF10B981)
                            "Completado" -> Color(0xFF3B82F6)
                            "Cancelado" -> Color(0xFFEF4444)
                            else -> VerviColors.TextGray
                        }
                        com.example.verviapp.ui.components.VerviStatusBadge(
                            text = status,
                            color = statusColor
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // Title + Price
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = detail?.title ?: "Cargando servicio...",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = VerviColors.TextDark,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = detail?.totalPriceText ?: "$0",
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

                    Spacer(Modifier.height(8.dp))

                    // Date
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarToday,
                            contentDescription = null,
                            tint = VerviColors.TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = detail?.dateText ?: "--",
                            fontSize = 14.sp,
                            color = VerviColors.TextSecondary
                        )
                    }

                    Spacer(Modifier.height(22.dp))

                    // Summary
                    SectionTitle(text = "Resumen del Trabajo")
                    Spacer(Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = VerviColors.CardBackground),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Text(
                            text = detail?.summary?.takeIf { it.isNotBlank() } ?: "Sin resumen disponible.",
                            modifier = Modifier.padding(16.dp),
                            fontSize = 15.sp,
                            lineHeight = 23.sp,
                            color = VerviColors.TextPrimary
                        )
                    }

                    Spacer(Modifier.height(22.dp))

                    // Evidence
                    SectionTitle(text = "Evidencia del Servicio")
                    Spacer(Modifier.height(10.dp))
                    val evidenceUrls = detail?.evidenceImageUrls ?: emptyList()
                    if (evidenceUrls.isNotEmpty()) {
                        EvidenceGrid(imageUrls = evidenceUrls)
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(VerviColors.BorderGray.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Outlined.Image,
                                    contentDescription = null,
                                    tint = VerviColors.TextGray,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    text = "Sin evidencia disponible",
                                    fontSize = 13.sp,
                                    color = VerviColors.TextGray
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(22.dp))

                    // Counterpart
                    SectionTitle(text = if (uiState.isOwner) "Cliente" else "Proveedor")
                    Spacer(Modifier.height(8.dp))
                    if (detail != null) {
                        PublisherCard(
                            name = detail.counterpartName,
                            ratingLine = detail.counterpartRatingText,
                            avatarUrl = detail.counterpartAvatarUrl,
                            showChat = false,
                            onAvatarClick = {
                                uiState.counterpartUserId?.let { navController.navigate("profile?userId=$it") }
                            }
                        )
                    }

                    Spacer(Modifier.height(88.dp))
                }
            }
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
private fun EvidenceGrid(imageUrls: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        imageUrls.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { url ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(160.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        AsyncImage(
                            model = url,
                            contentDescription = "Evidencia",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    }
                }
                if (row.size < 2) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}
