package com.example.verviapp.Presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.verviapp.ui.components.VerviButton
import com.example.verviapp.ui.components.VerviOutlinedButton
import com.example.verviapp.ui.components.VerviTextArea
import com.example.verviapp.ui.theme.VerviColors

@Composable
fun RequestCancelScreen(navController: NavController) {
    var selectedReason by remember { mutableStateOf("") }
    var additionalDetails by remember { mutableStateOf("") }
    var reasonMenuExpanded by remember { mutableStateOf(false) }
    val reasonOptions = remember {
        listOf(
            "No tengo disponibilidad",
            "No estoy de acuerdo con el precio",
            "El cliente no responde",
            "Otro motivo"
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F7F8))
    ) {
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
                    .padding(horizontal = 20.dp, vertical = 10.dp)
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
                        .background(Color(0xFFFEE2E2))
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = null,
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Confirmar Cancelación",
                    fontSize = 36.sp / 2,
                    fontWeight = FontWeight.Bold,
                    color = VerviColors.TextDark,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "¿Estás seguro de que deseas cancelar este servicio? Esta acción puede afectar tu calificación de usuario en Vervi.",
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = VerviColors.TextSecondary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Motivo de cancelación",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerviColors.TextDark
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box {
                    OutlinedTextField(
                        value = selectedReason,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Selecciona una opción", color = Color(0xFF9CA3AF)) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = VerviColors.TextSecondary
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { reasonMenuExpanded = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = VerviColors.BorderGray,
                            focusedBorderColor = VerviColors.Blue,
                            unfocusedContainerColor = Color(0xFFF8FAFC),
                            focusedContainerColor = Color.White
                        )
                    )
                    DropdownMenu(
                        expanded = reasonMenuExpanded,
                        onDismissRequest = { reasonMenuExpanded = false }
                    ) {
                        reasonOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedReason = option
                                    reasonMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                VerviTextArea(
                    label = "Detalles adicionales (opcional)",
                    value = additionalDetails,
                    onValueChange = { additionalDetails = it },
                    placeholder = "Cuéntanos más sobre el motivo para mejorar nuestro servicio...",
                    minLines = 4,
                    maxLines = 8
                )

                Spacer(modifier = Modifier.height(24.dp))

                VerviButton(
                    text = "Cancelar Servicio",
                    onClick = { navController.popBackStack() },
                    color = Color(0xFFDC2626),
                    height = 58.dp
                )

                Spacer(modifier = Modifier.height(12.dp))

                VerviOutlinedButton(
                    text = "Mantener Servicio",
                    onClick = { navController.popBackStack() },
                    color = VerviColors.Blue,
                    height = 58.dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Al cancelar, aceptas los términos de servicio de Vervi Colombia.",
                    fontSize = 12.sp,
                    color = VerviColors.TextSecondary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
