package com.example.verviapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.verviapp.ui.components.VerviButton
import com.example.verviapp.ui.components.VerviOutlinedButton
import com.example.verviapp.ui.components.VerviTextArea
import com.example.verviapp.ui.theme.VerviColors
import com.example.verviapp.viewmodel.RequestCancelEvent
import com.example.verviapp.viewmodel.RequestCancelViewModel

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun RequestCancelScreen(
    navController: NavController,
    requestId: Int,
    viewModel: RequestCancelViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val errorMessage = uiState.error
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var reasonMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(requestId) {
        viewModel.load(requestId)
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            if (event is RequestCancelEvent.Cancelled) {
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
                .verticalScroll(rememberScrollState())
                .imePadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .size(width = 48.dp, height = 5.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFCBD5E1))
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            androidx.compose.foundation.layout.Box(
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
                text = "Confirmar Cancelacion",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = VerviColors.TextDark,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "¿Estas seguro de que deseas cancelar este servicio? Esta accion puede afectar tu calificacion de usuario en Vervi.",
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = VerviColors.TextSecondary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Motivo de cancelacion",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = VerviColors.TextDark
            )
            Spacer(modifier = Modifier.height(6.dp))
            ExposedDropdownMenuBox(
                expanded = reasonMenuExpanded,
                onExpandedChange = {
                    if (uiState.reasonOptions.isNotEmpty()) {
                        reasonMenuExpanded = it
                    }
                }
            ) {
                OutlinedTextField(
                    value = uiState.selectedReason,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Selecciona una opcion", color = Color(0xFF9CA3AF)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = reasonMenuExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = VerviColors.BorderGray,
                        focusedBorderColor = VerviColors.Blue,
                        unfocusedContainerColor = Color(0xFFF8FAFC),
                        focusedContainerColor = Color.White
                    )
                )
                ExposedDropdownMenu(
                    expanded = reasonMenuExpanded,
                    onDismissRequest = { reasonMenuExpanded = false }
                ) {
                    uiState.reasonOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                viewModel.onReasonSelected(option)
                                reasonMenuExpanded = false
                            }
                        )
                    }
                }
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    color = Color(0xFFDC2626),
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            VerviTextArea(
                label = "Detalles adicionales (opcional)",
                value = uiState.additionalDetails,
                onValueChange = { viewModel.onAdditionalDetailsChange(it) },
                placeholder = "Cuentanos mas sobre el motivo para mejorar nuestro servicio...",
                minLines = 4,
                maxLines = 8
            )

            Spacer(modifier = Modifier.height(24.dp))

            VerviButton(
                text = if (uiState.isSubmitting) "Cancelando..." else "Cancelar Servicio",
                onClick = {
                    if (!uiState.isSubmitting) {
                        viewModel.cancelRequest()
                    }
                },
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
                text = "Al cancelar, aceptas los terminos de servicio de Vervi Colombia.",
                fontSize = 12.sp,
                color = VerviColors.TextSecondary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
