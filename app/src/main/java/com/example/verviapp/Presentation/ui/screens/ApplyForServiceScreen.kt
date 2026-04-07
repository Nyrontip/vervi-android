package com.example.verviapp.Presentation.ui.screens

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
import androidx.compose.foundation.layout.widthIn
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
import com.example.verviapp.Presentation.ui.components.VerviButton
import com.example.verviapp.Presentation.ui.components.VerviOutlinedButton
import com.example.verviapp.Presentation.ui.components.VerviTextField
import com.example.verviapp.Presentation.ui.theme.VerviColors

private val SheetBackdrop = Color(0xFFE2E8F0)
private val FieldMutedBg = Color(0xFFF8FAFC)
private val EvidenceBorder = Color(0xFFCBD5E1)
private val ModalSurface = Color(0xFFF6F7F8)

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
            .background(_root_ide_package_.com.example.verviapp.Presentation.ui.screens.SheetBackdrop)
            .navigationBarsPadding()
    ) {
        // Overlay area above the sheet (as in mockup)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color.Black.copy(alpha = 0.20f))
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = _root_ide_package_.com.example.verviapp.Presentation.ui.screens.ModalSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                _root_ide_package_.com.example.verviapp.Presentation.ui.screens.BottomSheetDragHandle()

                Text(
                    text = "Postularse al servicio",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = _root_ide_package_.com.example.verviapp.Presentation.ui.theme.VerviColors.TextDark,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Required message — label with orange asterisk
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "Mensaje de presentación ",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = _root_ide_package_.com.example.verviapp.Presentation.ui.theme.VerviColors.TextSecondary
                    )
                    Text(
                        text = "*",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = _root_ide_package_.com.example.verviapp.Presentation.ui.theme.VerviColors.OrangeSecondary
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
                    minLines = 4,
                    maxLines = 8,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = _root_ide_package_.com.example.verviapp.Presentation.ui.theme.VerviColors.BorderGray,
                        focusedBorderColor = _root_ide_package_.com.example.verviapp.Presentation.ui.theme.VerviColors.Blue,
                        unfocusedContainerColor = Color(0xFFF8FAFC),
                        focusedContainerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Precio propuesto (COP)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = _root_ide_package_.com.example.verviapp.Presentation.ui.theme.VerviColors.TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = proposedPrice,
                    onValueChange = { proposedPrice = it },
                    placeholder = { Text("$ Ej: 50.000", color = Color(0xFFAAAAAA)) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = _root_ide_package_.com.example.verviapp.Presentation.ui.theme.VerviColors.BorderGray,
                        focusedBorderColor = _root_ide_package_.com.example.verviapp.Presentation.ui.theme.VerviColors.Blue,
                        unfocusedContainerColor = Color(0xFFF8FAFC),
                        focusedContainerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Evidencia (opcional)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = _root_ide_package_.com.example.verviapp.Presentation.ui.theme.VerviColors.TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))

                _root_ide_package_.com.example.verviapp.Presentation.ui.screens.EvidenceDropZone(
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
                            checkedColor = _root_ide_package_.com.example.verviapp.Presentation.ui.theme.VerviColors.OrangeSecondary,
                            uncheckedColor = _root_ide_package_.com.example.verviapp.Presentation.ui.theme.VerviColors.TextGray
                        ),
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Confirmar disponibilidad inmediata",
                        fontSize = 14.sp,
                        color = _root_ide_package_.com.example.verviapp.Presentation.ui.theme.VerviColors.TextDark,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                _root_ide_package_.com.example.verviapp.Presentation.ui.components.VerviButton(
                    text = "Enviar Postulación",
                    onClick = { navController.popBackStack() },
                    color = _root_ide_package_.com.example.verviapp.Presentation.ui.theme.VerviColors.OrangeSecondary,
                    height = 54.dp,
                    modifier = Modifier.fillMaxWidth()
                )

                TextButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                ) {
                    Text(
                        text = "Cancelar",
                        color = _root_ide_package_.com.example.verviapp.Presentation.ui.theme.VerviColors.TextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
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
            .border(1.dp,
                _root_ide_package_.com.example.verviapp.Presentation.ui.screens.EvidenceBorder, RoundedCornerShape(14.dp))
            .background(_root_ide_package_.com.example.verviapp.Presentation.ui.screens.FieldMutedBg)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.CloudUpload,
            contentDescription = null,
            tint = _root_ide_package_.com.example.verviapp.Presentation.ui.theme.VerviColors.OrangeSecondary,
            modifier = Modifier.size(44.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Adjuntar imagen/evidencia",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = _root_ide_package_.com.example.verviapp.Presentation.ui.theme.VerviColors.TextDark
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Sube una foto de tu experiencia previa",
            fontSize = 13.sp,
            color = _root_ide_package_.com.example.verviapp.Presentation.ui.theme.VerviColors.TextSecondary
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
                color = _root_ide_package_.com.example.verviapp.Presentation.ui.theme.VerviColors.OrangeSecondary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable { onClearClick() }
                    .padding(4.dp)
            )
        }

        Box(modifier = Modifier.widthIn(max = 170.dp)) {
            _root_ide_package_.com.example.verviapp.Presentation.ui.components.VerviOutlinedButton(
                text = "+ Seleccionar",
                onClick = onSelectClick,
                color = _root_ide_package_.com.example.verviapp.Presentation.ui.theme.VerviColors.TextGray,
                height = 42.dp,
                fontSize = 14.sp
            )
        }
    }
}
