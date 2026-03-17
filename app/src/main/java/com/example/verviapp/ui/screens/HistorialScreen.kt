package com.example.verviapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import coil.compose.AsyncImage
import com.example.verviapp.ui.components.VerviBottomBar
import com.example.verviapp.ui.components.VerviTopBar
import com.example.verviapp.ui.theme.VerviColors

data class HistorialItem(
    val titulo: String,
    val prestador: String,
    val fecha: String,
    val precio: String,
    val rating: Double?,
    val imageUrl: String
)

@Composable
fun HistorialScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }

    val historialCliente = listOf(
        HistorialItem(
            titulo = "Lavado de alfombras",
            prestador = "Sofía García",
            fecha = "12 Oct 2023",
            precio = "$85.000 COP",
            rating = 5.0,
            imageUrl = "https://images.unsplash.com/photo-1558618666-fcd25c85f82e?w=200&h=200&fit=crop"
        ),
        HistorialItem(
            titulo = "Reparación Eléctrica",
            prestador = "Carlos Martínez",
            fecha = "05 Oct 2023",
            precio = "$120.000 COP",
            rating = 4.0,
            imageUrl = "https://images.unsplash.com/photo-1621905251189-08b45d6a269e?w=200&h=200&fit=crop"
        ),
        HistorialItem(
            titulo = "Paseo de Mascotas",
            prestador = "Laura Beltrán",
            fecha = "28 Sep 2023",
            precio = "$35.000 COP",
            rating = null,
            imageUrl = "https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=200&h=200&fit=crop"
        ),
        HistorialItem(
            titulo = "Limpieza de Vidrios",
            prestador = "Mateo Ruiz",
            fecha = "15 Sep 2023",
            precio = "$60.000 COP",
            rating = 4.8,
            imageUrl = "https://images.unsplash.com/photo-1563453392212-326f5e854473?w=200&h=200&fit=crop"
        )
    )

    val historialPrestador = listOf(
        HistorialItem(
            titulo = "Plomería general",
            prestador = "Ana López",
            fecha = "10 Oct 2023",
            precio = "$95.000 COP",
            rating = 4.5,
            imageUrl = "https://images.unsplash.com/photo-1585704032915-c3400ca199e7?w=200&h=200&fit=crop"
        ),
        HistorialItem(
            titulo = "Pintura de interiores",
            prestador = "Diego Romero",
            fecha = "01 Oct 2023",
            precio = "$200.000 COP",
            rating = 5.0,
            imageUrl = "https://images.unsplash.com/photo-1562259949-e8e7689d7828?w=200&h=200&fit=crop"
        )
    )

    val items = if (selectedTab == 0) historialCliente else historialPrestador

    Scaffold(
        topBar = {
            VerviTopBar(
                title = "Historial de servicios",
                onBack = { navController.popBackStack() }
            )
        },
        bottomBar = { VerviBottomBar(navController) },
        containerColor = VerviColors.BgColor
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = VerviColors.Blue,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = VerviColors.Blue
                    )
                }
            ) {
                listOf("Como Cliente", "Como Prestador").forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 14.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) VerviColors.Blue else VerviColors.TextGray
                            )
                        }
                    )
                }
            }

            if (items.isEmpty()) {
                HistorialEmptyState()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items) { item ->
                        HistorialCard(item = item)
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@Composable
private fun HistorialCard(item: HistorialItem) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.titulo,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerviColors.TextDark
                )

                Text(
                    text = "${item.prestador} • ${item.fecha}",
                    fontSize = 13.sp,
                    color = VerviColors.TextGray
                )

                Text(
                    text = item.precio,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerviColors.Blue
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (item.rating != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StarRating(rating = item.rating)
                        Text(
                            text = "Ver detalles ›",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = VerviColors.Blue,
                            modifier = Modifier.clickable { }
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .border(1.dp, VerviColors.TextGray, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "SIN CALIFICAR",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = VerviColors.TextGray
                            )
                        }
                        Text(
                            text = "Calificar ahora ★",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = VerviColors.StarFilled,
                            modifier = Modifier.clickable { }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.titulo,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun StarRating(rating: Double) {
    val fullStars = rating.toInt()
    val hasHalf = (rating - fullStars) >= 0.5

    Row(verticalAlignment = Alignment.CenterVertically) {
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= fullStars) Icons.Filled.Star
                else if (i == fullStars + 1 && hasHalf) Icons.Filled.Star
                else Icons.Filled.StarBorder,
                contentDescription = null,
                tint = if (i <= fullStars || (i == fullStars + 1 && hasHalf))
                    VerviColors.StarFilled else VerviColors.StarEmpty,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = rating.toString(),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = VerviColors.TextDark
        )
    }
}

@Composable
private fun HistorialEmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = VerviColors.StarEmpty,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Aún no tienes historial",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = VerviColors.TextDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tus servicios completados aparecerán aquí",
                fontSize = 14.sp,
                color = VerviColors.TextGray
            )
        }
    }
}
