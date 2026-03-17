package com.example.verviapp

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.verviapp.ui.components.VerviFooterText
import com.example.verviapp.ui.theme.VerviColors
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var progress by remember { mutableStateOf(0f) }

    // Anima el progreso suavemente al valor objetivo
    val animatedProgress by animateFloatAsState(
        targetValue   = progress,
        animationSpec = tween(durationMillis = 500, easing = LinearEasing),
        label         = "progressAnimation"
    )

    // Arranca la animación y al terminar abre LoginActivity
    LaunchedEffect(Unit) {
        progress = 1f
        delay(500) // espera que termine la animación
        navController.navigate("home")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.weight(0.8f))

        // ── Bloque central: logo, nombre, subtítulo, barra ──
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter            = painterResource(id = R.drawable.logo_vervi),
                contentDescription = "Logo Vervi",
                modifier           = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text          = "Vervi",
                fontSize      = 36.sp,
                fontWeight    = FontWeight.Bold,
                color         = VerviColors.TextDark,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text          = "COLOMBIA",
                fontSize      = 13.sp,
                fontWeight    = FontWeight.Medium,
                color         = VerviColors.TextGray,
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Texto "Cargando..." alineado a la izquierda
            Text(
                text     = "Cargando...",
                fontSize = 14.sp,
                color    = VerviColors.TextMid,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Barra de progreso: fondo gris + relleno azul animado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(VerviColors.ProgressBg, RoundedCornerShape(3.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .fillMaxHeight()
                        .background(VerviColors.Blue, RoundedCornerShape(3.dp))
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Footer global — navigationBarsPadding y padding bottom ya están en el componente
        VerviFooterText(
            text     = "Conectando servicios en Colombia",
            modifier = Modifier.navigationBarsPadding().padding(bottom = 16.dp)
        )
    }
}
