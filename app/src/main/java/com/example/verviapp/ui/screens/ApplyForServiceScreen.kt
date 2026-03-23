package com.example.verviapp.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.verviapp.ui.components.VerviButton
import com.example.verviapp.ui.components.VerviOutlinedButton
import com.example.verviapp.ui.components.VerviTextField
import com.example.verviapp.ui.theme.VerviColors

private val SheetBackdrop = Color(0xFFE2E8F0)
private val FieldMutedBg = Color(0xFFF8FAFC)
private val EvidenceBorder = Color(0xFFCBD5E1)

/**
 * Apply-for-service form (bottom-sheet style). UI strings in Spanish; state names in English.
 */
@Composable
fun ApplyForServiceScreen(navController: NavController) {
    var presentationMessage by remember { mutableStateOf("") }
    var proposedPrice by remember { mutableStateOf("") }
    var evidenceUri by remember { mutableStateOf<Uri?>(null) }
    var immediateAvailability by remember { mutableStateOf(false) }

    val pickEvidence = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> evidenceUri = uri }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SheetBackdrop)
            .navigationBarsPadding()
    ) {
        Spacer(modifier = Modifier.height(28.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                BottomSheetDragHandle()

                Text(
                    text = "Postularse al servicio",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerviColors.TextDark,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Required message — label with orange asterisk
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "Mensaje de presentación ",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerviColors.TextSecondary
                    )
                    Text(
                        text = "*",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerviColors.OrangeSecondary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = presentationMessage,
                    onValueChange = { presentationMessage = it },
                    placeholder = {
                        Text(
                            "Cuéntale al cliente por qué eres el mejor para este trabajo...",
                            color = Color(0xFFAAAAAA)
                        )
                    },
                    minLines = 5,
                    maxLines = 10,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = VerviColors.BorderGray,
                        focusedBorderColor = VerviColors.Blue,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                VerviTextField(
                    label = "Precio propuesto (COP)",
                    value = proposedPrice,
                    onValueChange = { proposedPrice = it },
                    placeholder = "$ Ej: 50.000",
                    labelSize = 12.sp,
                    keyboardType = KeyboardType.Decimal
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Evidencia (opcional)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerviColors.TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))

                EvidenceDropZone(
                    evidenceUri = evidenceUri,
                    onSelectClick = {
                        pickEvidence.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onClearClick = { evidenceUri = null }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = immediateAvailability,
                        onCheckedChange = { immediateAvailability = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = VerviColors.OrangeSecondary,
                            uncheckedColor = VerviColors.TextGray
                        )
                    )
                    Text(
                        text = "Confirmar disponibilidad inmediata",
                        fontSize = 14.sp,
                        color = VerviColors.TextDark,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                VerviButton(
                    text = "Enviar Postulación",
                    onClick = { navController.popBackStack() },
                    color = VerviColors.OrangeSecondary,
                    height = 54.dp,
                    modifier = Modifier.fillMaxWidth()
                )

                TextButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    Text(
                        text = "Cancelar",
                        color = VerviColors.TextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun BottomSheetDragHandle() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(5.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFCBD5E1))
        )
    }
}

@Composable
private fun EvidenceDropZone(
    evidenceUri: Uri?,
    onSelectClick: () -> Unit,
    onClearClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, EvidenceBorder, RoundedCornerShape(14.dp))
            .background(FieldMutedBg)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.CloudUpload,
            contentDescription = null,
            tint = VerviColors.OrangeSecondary,
            modifier = Modifier.size(44.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Adjuntar imagen/evidencia",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = VerviColors.TextDark
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Sube una foto de tu experiencia previa",
            fontSize = 13.sp,
            color = VerviColors.TextSecondary
        )
        Spacer(modifier = Modifier.height(14.dp))

        if (evidenceUri != null) {
            AsyncImage(
                model = evidenceUri,
                contentDescription = "Evidencia",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Quitar",
                color = VerviColors.OrangeSecondary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable { onClearClick() }
                    .padding(4.dp)
            )
        }

        VerviOutlinedButton(
            text = "+ Seleccionar",
            onClick = onSelectClick,
            color = VerviColors.TextGray,
            height = 42.dp,
            fontSize = 14.sp
        )
    }
}
