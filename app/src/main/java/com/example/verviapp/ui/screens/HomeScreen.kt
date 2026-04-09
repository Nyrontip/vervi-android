package com.example.verviapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.verviapp.ui.components.*
import com.example.verviapp.ui.theme.VerviColors
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.verviapp.viewModel.state.Service
import com.example.verviapp.viewModel.HomeViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }  // estado local de UI
    var selectedCat by remember { mutableStateOf("Todos") }  // estado local de UI
    val categorias = listOf("Todos", "Carpintería", "Limpieza", "Electricidad")
    
    Scaffold(
        topBar = {
            VerviTopBar(
                title   = "Vervi",
                onBack  = null,
                actions = {
                    IconButton(onClick        = {
                        navController.navigate("login")
                    }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil",
                            tint = VerviColors.TextDark)
                    }
                    IconButton(onClick = {
                        navController.navigate("notifications")
                    }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notificaciones",
                            tint = VerviColors.TextDark)
                    }
                }
            )
        },
        bottomBar = {
            VerviBottomBar(navController)
        },
        floatingActionButton = { HomeFabs(navController) },
        containerColor = VerviColors.BgColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(2.dp))

            // Buscador con ícono de lupa
            VerviSearchField(
                value         = searchQuery,
                onValueChange = { searchQuery = it; viewModel.onSearchChange(it) },
                placeholder   = "¿Qué servicio necesitas?",
                leadingIcon   = Icons.Default.Search
            )

            Spacer(modifier = Modifier.height(14.dp))

            // VerviChips global — categorías seleccionables con scroll horizontal
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                VerviChips(
                    opciones  = categorias,
                    selected  = selectedCat,
                    onSelect = { selectedCat = it; viewModel.onCategoryChange(it) }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text("Solicitudes Recientes", fontSize = 18.sp,
                fontWeight = FontWeight.Bold, color = VerviColors.TextDark)
            Text("Cerca de tu ubicación actual", fontSize = 13.sp, color = VerviColors.TextGray)

            Spacer(modifier = Modifier.height(12.dp))

            state.services.forEach { servicio ->
                ServiceCard(
                    servicio = servicio,
                    onDetalle = { navController.navigate("request/details") }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

// ── ServiceCard — exclusivo de Home ───────────────────────
@Composable
private fun ServiceCard(servicio: Service, onDetalle: () -> Unit) {
    Card(
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Imagen con badge de precio superpuesto
            Box {
                Image(
                    painter            = painterResource(id = servicio.imageRes),
                    contentDescription = servicio.title,
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier.fillMaxWidth().height(160.dp)
                )
                // Badge precio — fondo blanco, texto azul (usa VerviBadge global)
                Box(modifier = Modifier.align(Alignment.TopEnd).padding(10.dp)) {
                    VerviBadge(text     = servicio.price, color    = VerviColors.Blue, fontSize = 13.sp,)
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                // Título + badge URGENTE
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(servicio.title, fontSize = 15.sp, fontWeight = FontWeight.Bold,
                        color = VerviColors.TextDark, modifier = Modifier.weight(1f))
                    if (servicio.isUrgent) {
                        Spacer(modifier = Modifier.width(8.dp))
                        // Badge URGENTE outlined — borde azul, sin fondo
                        VerviBadge(text = "URGENTE", color = VerviColors.Blue, outlined = true)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Ubicación
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null,
                        tint = VerviColors.TextGray, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(servicio.location, fontSize = 13.sp, color = VerviColors.TextGray)
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Postulaciones + botón
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Group, contentDescription = null,
                            tint = VerviColors.TextGray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${servicio.applicationCount} Postulaciones",
                            fontSize = 13.sp, color = VerviColors.TextGray)
                    }
                    VerviButton(
                        text      = "Ver detalle",
                        onClick   = onDetalle, fillWidth = false, height    = 38.dp, fontSize  = 13.sp
                    )
                }
            }
        }
    }
}

// ── HomeFabs — dos FABs mismo tamaño apilados ─────────────
@Composable
private fun HomeFabs(navController: NavController) {
    Column(horizontalAlignment = Alignment.End) {
        // FAB perfil — tamaño intermedio fijo
        FloatingActionButton(
            onClick        = { navController.navigate("prestadores")},
            containerColor = VerviColors.Blue,
            contentColor   = Color.White,
            modifier       = Modifier.size(52.dp)    // mismo tamaño que el naranja
        ) {
            Icon(Icons.Default.Person, contentDescription = "Perfil",
                modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))
        // FAB crear solicitud — naranja secundario, mismo tamaño
        FloatingActionButton(
            onClick        = { navController.navigate("request/new") },
            containerColor = VerviColors.OrangeSecondary,
            contentColor   = Color.White,
            modifier       = Modifier.size(52.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Nueva solicitud",
                modifier = Modifier.size(22.dp))
        }
    }
}