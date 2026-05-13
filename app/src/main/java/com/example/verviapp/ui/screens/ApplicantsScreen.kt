package com.example.verviapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.verviapp.ui.components.VerviBottomBar
import com.example.verviapp.ui.components.VerviSmallButton
import com.example.verviapp.ui.components.VerviTopBar
import com.example.verviapp.ui.theme.VerviColors
import com.example.verviapp.viewmodel.ApplicantsViewModel

@Composable
fun ApplicantsScreen(
    navController: NavController,
    vm: ApplicantsViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()

    Scaffold(
        topBar = {
            VerviTopBar(
                title = "Postulaciones",
                onBack = { navController.popBackStack() }
            )
        },
        bottomBar = { VerviBottomBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(VerviColors.BgColor)
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = VerviColors.Blue)
                    }
                }

                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Warning, null, tint = VerviColors.CancelRed, modifier = Modifier.size(48.dp))
                            Spacer(Modifier.height(12.dp))
                            Text(state.error!!, fontSize = 14.sp, color = VerviColors.TextDark)
                            Spacer(Modifier.height(16.dp))
                            VerviSmallButton(text = "Reintentar", color = VerviColors.Blue, onClick = { vm.retry() })
                        }
                    }
                }

                state.applicants.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Person, null, tint = VerviColors.TextGray, modifier = Modifier.size(64.dp))
                            Spacer(Modifier.height(16.dp))
                            Text("Sin postulaciones aún", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = VerviColors.TextDark)
                            Spacer(Modifier.height(4.dp))
                            Text("Los proveedores postularán a esta solicitud", fontSize = 13.sp, color = VerviColors.TextGray)
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.applicants, key = { it.id }) { applicant ->
                            ApplicantCard(
                                applicant = applicant,
                                onAccept = { vm.accept(applicant.id) },
                                onReject = { vm.reject(applicant.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ApplicantCard(
    applicant: com.example.verviapp.viewmodel.ApplicantItem,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    val statusColor = when (applicant.status) {
        "ACCEPTED" -> Color(0xFF10B981)
        "REJECTED" -> Color(0xFFEF4444)
        else -> Color(0xFFF59E0B)
    }
    val statusLabel = when (applicant.status) {
        "ACCEPTED" -> "Aceptado"
        "REJECTED" -> "Rechazado"
        else -> "Pendiente"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar
                Box(modifier = Modifier.size(52.dp).clip(CircleShape)) {
                    if (applicant.avatarUrl != null) {
                        AsyncImage(
                            model = applicant.avatarUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize().background(VerviColors.BorderGray.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, null, tint = VerviColors.TextGray, modifier = Modifier.size(28.dp))
                        }
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(applicant.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = VerviColors.TextDark)
                    Spacer(Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        applicant.rating?.let { rating ->
                            Text("★ $rating", fontSize = 13.sp, color = Color(0xFFFFC107))
                            Spacer(Modifier.width(8.dp))
                        }
                        Text(applicant.proposedPrice, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = VerviColors.OrangeSecondary)
                    }
                    if (applicant.isAvailable) {
                        Text("Disponible ahora", fontSize = 11.sp, color = Color(0xFF10B981))
                    }
                }
                // Status badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(statusColor.copy(alpha = 0.1f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        statusLabel.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Message
            Text(
                text = applicant.message,
                fontSize = 13.sp,
                color = VerviColors.TextSecondary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            // Actions
            if (applicant.status == "PENDING") {
                Spacer(Modifier.height(12.dp))
                HorizontalDivider(color = VerviColors.BorderGray)
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VerviSmallButton(
                        text = "Rechazar",
                        color = VerviColors.CancelRed,
                        onClick = onReject,
                        modifier = Modifier.weight(1f)
                    )
                    VerviSmallButton(
                        text = "Aceptar",
                        color = Color(0xFF10B981),
                        onClick = onAccept,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
