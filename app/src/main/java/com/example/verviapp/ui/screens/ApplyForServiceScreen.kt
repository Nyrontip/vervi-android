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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.verviapp.ui.components.VerviButton
import com.example.verviapp.ui.components.VerviOutlinedButton
import com.example.verviapp.ui.theme.VerviColors
import com.example.verviapp.viewmodel.ApplyForServiceEvent
import com.example.verviapp.viewmodel.ApplyForServiceViewModel

private val SheetBackdrop = Color(0xFFE2E8F0)
private val FieldMutedBg = Color(0xFFF8FAFC)
private val EvidenceBorder = Color(0xFFCBD5E1)

/**
 * Apply-for-service form (bottom-sheet style). UI strings in Spanish; state names in English.
 */
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun ApplyForServiceScreen(
    navController: NavController,
    requestId: Int,
    vm: ApplyForServiceViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val errorMessage = uiState.error
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val pickEvidence = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> vm.onEvidenceSelected(uri?.toString()) }

    LaunchedEffect(requestId) {
        vm.load(requestId)
    }

    LaunchedEffect(vm) {
        vm.events.collect { event ->
            if (event is ApplyForServiceEvent.Submitted) {
                navController.popBackStack()
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = { navController.popBackStack() },
        sheetState = sheetState,
        containerColor = VerviColors.BottomSheetBackground,
        scrimColor = VerviColors.Overlay
    ) {
        Column(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        BottomSheetDragHandle()

        Text(
            text = "Postularse al servicio",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = VerviColors.TextDark,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        if (uiState.requestTitle.isNotBlank()) {
            Text(
                text = uiState.requestTitle,
                fontSize = 13.sp,
                color = VerviColors.TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

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
                    value = uiState.presentationMessage,
                    onValueChange = { vm.onPresentationMessageChange(it) },
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
                        unfocusedBorderColor = VerviColors.BorderGray,
                        focusedBorderColor = VerviColors.Blue,
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
                    color = VerviColors.TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = uiState.proposedPrice,
                    onValueChange = { vm.onProposedPriceChange(it) },
                    placeholder = { Text("$ Ej: 50.000", color = Color(0xFFAAAAAA)) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = VerviColors.BorderGray,
                        focusedBorderColor = VerviColors.Blue,
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
                    color = VerviColors.TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))

                EvidenceDropZone(
                    evidenceUri = uiState.evidenceUri,
                    onSelectClick = {
                        pickEvidence.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onClearClick = { vm.onEvidenceSelected(null) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = uiState.immediateAvailability,
                        onCheckedChange = { vm.onImmediateAvailabilityChange(it) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = VerviColors.OrangeSecondary,
                            uncheckedColor = VerviColors.TextGray
                        ),
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Confirmar disponibilidad inmediata",
                        fontSize = 14.sp,
                        color = VerviColors.TextDark,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                VerviButton(
                    text = if (uiState.isSubmitting) "Enviando..." else "Enviar Postulacion",
                    onClick = {
                        if (!uiState.isSubmitting) {
                            vm.submit()
                        }
                    },
                    color = VerviColors.OrangeSecondary,
                    height = 54.dp,
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage,
                        color = Color(0xFFDC2626),
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                TextButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                ) {
                    Text(
                        text = "Cancelar",
                        color = VerviColors.TextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
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
    evidenceUri: String?,
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

        Box(modifier = Modifier.widthIn(max = 170.dp)) {
            VerviOutlinedButton(
                text = "+ Seleccionar",
                onClick = onSelectClick,
                color = VerviColors.TextGray,
                height = 42.dp,
                fontSize = 14.sp
            )
        }
    }
}
