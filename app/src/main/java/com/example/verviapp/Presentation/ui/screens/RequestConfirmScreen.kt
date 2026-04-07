package com.example.verviapp.Presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.verviapp.ui.components.VerviButton
import com.example.verviapp.ui.theme.VerviColors

@Composable
fun RequestConfirmScreen(navController: NavController) {
    var paymentReceiptConfirmed by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F7F8))
    ) {
        // Simula fondo difuminado detrás del modal como en la referencia.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.25f))
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 48.dp, height = 5.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFFCBD5E1))
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEFF6FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircleOutline,
                        contentDescription = null,
                        tint = VerviColors.Blue,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Confirmar Servicio",
                    fontSize = 36.sp / 2,
                    fontWeight = FontWeight.Bold,
                    color = VerviColors.TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "¿Estás seguro de marcar este servicio como completado? Esto liberará el pago al proveedor.",
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = VerviColors.TextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF8FAFC))
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = paymentReceiptConfirmed,
                        onCheckedChange = { paymentReceiptConfirmed = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = VerviColors.Blue,
                            uncheckedColor = VerviColors.TextGray
                        )
                    )
                    Text(
                        text = "Confirmar recibo de pago (COP)",
                        fontSize = 16.sp,
                        color = VerviColors.TextDark
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                VerviButton(
                    text = "Confirmar",
                    onClick = { navController.popBackStack() },
                    color = VerviColors.Primary,
                    height = 58.dp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Volver",
                    fontSize = 34.sp / 2,
                    fontWeight = FontWeight.SemiBold,
                    color = VerviColors.TextSecondary,
                    modifier = Modifier
                        .clickable { navController.popBackStack() }
                        .padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}
