package com.example.verviapp.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.verviapp.ui.components.*
import com.example.verviapp.ui.theme.VerviColors
import com.example.verviapp.viewmodel.state.Provider
import com.example.verviapp.viewmodel.PrestadoresViewModel

@Composable
fun PrestadoresScreen(navController: NavController, viewModel: PrestadoresViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar    = {
            VerviTopBar(
                title = "Directorio de Prestadores",
                onBack = { navController.popBackStack() })
        },
        bottomBar = {
            VerviBottomBar(
                navController
            )
        },
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

            VerviSearchField(
                value = state.searchQuery,
                onValueChange = viewModel::onSearchChange,
                placeholder = "Buscar...",
                onSearchClick = viewModel::onSearchDone
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Chips de categoría con scroll horizontal — reutiliza VerviChips global
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                VerviChips(
                    opciones = state.categories,
                    selected = state.selectedCategory,
                    onSelect = viewModel::onCategoryChange
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                VerviLoadingState()
            }

            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage!!,
                    color = Color.Red,
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            state.providers.forEach { prestador ->
                PrestadorCard(
                    prestador = prestador,
                    onVerPerfil = { navController.navigate("profile?userId=${prestador.id}") })
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PrestadorCard(prestador: Provider, onVerPerfil: () -> Unit) {
    Card(
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Fila superior: foto + info
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Foto del prestador con esquinas redondeadas
                AsyncImage(
                    model = prestador.imageUrl,
                    contentDescription = prestador.name,
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // Nombre + precio en la misma fila extremos opuestos
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.Top
                    ) {
                        Text(prestador.name, fontSize = 16.sp,
                            fontWeight = FontWeight.Bold, color = VerviColors.TextDark)

                        // Precio alineado a la derecha con "Desde" encima
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Desde", fontSize = 11.sp, color = VerviColors.TextGray)
                            Text(prestador.price, fontSize = 14.sp,
                                fontWeight = FontWeight.Bold, color = VerviColors.OrangeSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Badge especialidad + rating en la misma fila
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        VerviBadge(
                            text = prestador.specialty,
                            color = VerviColors.TextDark,
                            fontSize = 10.sp,
                            outlined = true
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Estrella + calificación + número de reseñas
                        Icon(Icons.Default.Star, contentDescription = null,
                            tint = VerviColors.OrangeSecondary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("${prestador.rating}", fontSize = 13.sp,
                            fontWeight = FontWeight.Bold, color = VerviColors.TextDark)
                        Text(" (${prestador.reviewCount})", fontSize = 12.sp, color = VerviColors.TextGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            VerviOutlinedButton(
                text = "Ver Perfil",
                onClick = onVerPerfil
            )
        }
    }
}
