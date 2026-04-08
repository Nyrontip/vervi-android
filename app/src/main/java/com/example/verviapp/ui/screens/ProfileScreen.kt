package com.example.verviapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.verviapp.ui.components.*
import com.example.verviapp.ui.theme.VerviColors
import com.example.verviapp.viewModel.ProfileViewModel

@Composable
fun ProfileScreen(navController: NavController, userId: String? = null, viewModel: ProfileViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadProfile(userId)     // null carga el perfil local
    }

    Scaffold(
        topBar = {
            VerviTopBar(
                title   = "Mi Perfil",
                onBack  = { navController.popBackStack() },
                actions = {
                    IconButton(onClick = { navController.navigate("editProfile") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Ajustes",
                            tint = VerviColors.TextDark)
                    }
                }
            )
        },
        bottomBar = {
            VerviBottomBar(navController)
        },
        containerColor = VerviColors.BgColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(2.dp))

            // ── Foto circular con borde blanco ──────────────────
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(3.dp, Color.White, CircleShape)
            ) {
                Image(
                    painter            = painterResource(id = R.drawable.login_hero),
                    contentDescription = "Foto de perfil",
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Nombre ──────────────────────────────────────────
            Text(state.user.name, fontSize = 22.sp, fontWeight = FontWeight.Bold,
                color = VerviColors.TextDark)

            Spacer(modifier = Modifier.height(4.dp))

            // ── Rating ──────────────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null,
                    tint = VerviColors.OrangeSecondary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("${state.user.rating}", fontSize = 14.sp, fontWeight = FontWeight.Bold,
                    color = VerviColors.TextDark)
                Text(" (${state.user.reviewCount} reseñas)", fontSize = 13.sp, color = VerviColors.TextGray)
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(state.user.location, fontSize = 13.sp, color = VerviColors.TextGray)

            Spacer(modifier = Modifier.height(16.dp))

            // ── Botones Editar / Cerrar sesión ──────────────────
            VerviOutlinedButton(text = "Editar Perfil", onClick = { navController.navigate("editProfile") })
            Spacer(modifier = Modifier.height(8.dp))
            VerviOutlinedButton(text = "Cerrar Sesión", onClick = { navController.navigate("login") },
                color = Color(0xFFD32F2F))  // rojo para acción destructiva

            Spacer(modifier = Modifier.height(16.dp))

            // ── Badges de rol — Cliente / Prestador ─────────────
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                VerviBadge(text = "Cliente",   color = VerviColors.Blue, outlined = true, fontSize= 12.sp)
                VerviBadge(text = "Prestador", color = VerviColors.Blue, outlined = true, fontSize= 12.sp)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── Bio ─────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(VerviColors.BgColor)
                    .padding(14.dp)
            ) {
                Text(
                    text      = "\"${state.user.bio}\"",
                    fontSize  = 13.sp,
                    color     = VerviColors.TextDark,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Sección Categorías ──────────────────────────────
            SectionLabel("CATEGORÍAS")
            Spacer(modifier = Modifier.height(8.dp))

            // Chips de categoría NO seleccionables — VerviBadge outlined
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.user.categories.forEach { category ->
                    VerviBadge(text = category, color = VerviColors.TextDark, outlined = true, fontSize=13.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Precio sugerido ─────────────────────────────────
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Precio sugerido: ", fontSize = 14.sp, color = VerviColors.TextDark,
                    fontWeight = FontWeight.Medium)
                Text("${state.user.suggestedPrice} COP/hora", fontSize = 14.sp, color = VerviColors.Blue,
                    fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = VerviColors.BgColor, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            // ── Sección Actividad ───────────────────────────────
            SectionLabel("ACTIVIDAD")
            Spacer(modifier = Modifier.height(10.dp))

            // VerviActivityItem
            Card(
                shape    = RoundedCornerShape(12.dp),
                colors   = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                VerviActivityItem(
                    icon       = Icons.Default.ListAlt,
                    titulo     = "Mis Solicitudes",
                    subtitulo  = "Ver tus pedidos pendientes",
                    onClick    = { navController.navigate("requests/management") }
                )
                HorizontalDivider(
                    modifier  = Modifier.padding(horizontal = 16.dp),
                    color     = VerviColors.BgColor,
                    thickness = 1.dp
                )
                VerviActivityItem(
                    icon      = Icons.Default.History,
                    titulo    = "Historial de Servicios",
                    subtitulo = "Servicios completados y recibos",
                    onClick   = { navController.navigate("services/history") }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Stats: Proyectos / Solicitudes ──────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(numero = "${state.user.projectCount}", label = "PROYECTOS",  modifier = Modifier.weight(1f))
                StatCard(numero = "${state.user.requestCount}", label = "SOLICITUDES", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ── SectionLabel — label de sección en mayúsculas (exclusivo de perfil) ──
@Composable
private fun SectionLabel(text: String) {
    Text(
        text       = text,
        fontSize   = 13.sp,
        fontWeight = FontWeight.Bold,
        color      = VerviColors.Blue,
        letterSpacing = 1.5.sp,
        modifier   = Modifier.fillMaxWidth()
    )
}

// ── StatCard — tarjeta de estadística número + label (exclusiva de perfil) ──
@Composable
private fun StatCard(numero: String, label: String, modifier: Modifier = Modifier) {
    Card(
        shape    = RoundedCornerShape(12.dp),
        colors   = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
    ) {
        Column(
            modifier              = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalAlignment   = Alignment.CenterHorizontally
        ) {
            Text(numero, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = VerviColors.Blue)
            Spacer(modifier = Modifier.height(2.dp))
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                color = VerviColors.TextGray, letterSpacing = 1.sp)
        }
    }
}