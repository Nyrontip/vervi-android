package com.example.verviapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import com.example.verviapp.ui.components.*
import com.example.verviapp.ui.theme.VerviAppTheme
import com.example.verviapp.ui.theme.VerviColors

// ── Modelo de datos local ──────────────────────────────────
data class Prestador(
    val nombre: String,
    val especialidad: String,
    val precio: String,
    val rating: Float,
    val reviews: Int,
    val imagen: Int
)

class PrestadoresActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { VerviAppTheme { PrestadoresScreen(onBack = { finish() }) } }
    }
}

@Composable
fun PrestadoresScreen(onBack: () -> Unit = {}) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCat by remember { mutableStateOf("Todos") }

    val categorias   = listOf("Todos", "Carpinteros", "Plomeros", "Electricistas")

    // Datos quemados — 3 prestadores de ejemplo
    val prestadores = listOf(
        Prestador("Carlos Ruiz",     "PLOMERO",      "$80.000 COP", 4.9f, 48, R.drawable.login_hero),
        Prestador("Mateo Gómez",     "CARPINTERO",   "$75.000 COP", 4.7f, 32, R.drawable.login_hero),
        Prestador("Andrés Restrepo", "ELECTRICISTA", "$95.000 COP", 4.9f, 15, R.drawable.login_hero)
    )

    Scaffold(
        topBar    = { VerviTopBar(title = "Directorio de Prestadores", onBack = onBack) },
        bottomBar = { VerviBottomBar() },
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
                value         = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder   = "Buscar...",
                leadingIcon   = Icons.Default.Search
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Chips de categoría con scroll horizontal — reutiliza VerviChips global
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                VerviChips(opciones = categorias, selected = selectedCat, onSelect = { selectedCat = it })
            }

            Spacer(modifier = Modifier.height(16.dp))

            prestadores.forEach { prestador ->
                PrestadorCard(prestador = prestador, onVerPerfil = { /* TODO: ir a perfil */ })
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PrestadorCard(prestador: Prestador, onVerPerfil: () -> Unit) {
    Card(
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Fila superior: foto + info
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Foto del prestador con esquinas redondeadas
                Image(
                    painter            = painterResource(id = prestador.imagen),
                    contentDescription = prestador.nombre,
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
                        Text(prestador.nombre, fontSize = 16.sp,
                            fontWeight = FontWeight.Bold, color = VerviColors.TextDark)

                        // Precio alineado a la derecha con "Desde" encima
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Desde", fontSize = 11.sp, color = VerviColors.TextGray)
                            Text(prestador.precio, fontSize = 14.sp,
                                fontWeight = FontWeight.Bold, color = VerviColors.OrangeSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Badge especialidad + rating en la misma fila
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        VerviBadge(text     = prestador.especialidad,  color    = VerviColors.TextDark, fontSize = 10.sp, outlined = true)

                        Spacer(modifier = Modifier.width(8.dp))

                        // Estrella + calificación + número de reseñas
                        Icon(Icons.Default.Star, contentDescription = null,
                            tint = VerviColors.OrangeSecondary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("${prestador.rating}", fontSize = 13.sp,
                            fontWeight = FontWeight.Bold, color = VerviColors.TextDark)
                        Text(" (${prestador.reviews})", fontSize = 12.sp, color = VerviColors.TextGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            VerviOutlinedButton(text = "Ver Perfil", onClick = onVerPerfil)
        }
    }
}