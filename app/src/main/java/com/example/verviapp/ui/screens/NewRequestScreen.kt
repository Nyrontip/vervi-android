package com.example.verviapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.verviapp.ui.components.AttachmentSlot
import com.example.verviapp.ui.components.VerviFooterText
import com.example.verviapp.ui.components.VerviTextArea
import com.example.verviapp.ui.components.VerviTextField
import com.example.verviapp.ui.components.VerviTopBar
import com.example.verviapp.ui.theme.VerviColors
import com.example.verviapp.viewmodel.NewRequestEvent
import com.example.verviapp.viewmodel.NewRequestViewModel

/**
 * New request form screen (mockup: form + attachments + CTA).
 * Uses [VerviTopBar], [VerviTextField], [VerviTextArea], [VerviFooterText].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRequestScreen(
    navController: NavController,
    viewModel: NewRequestViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val categoriesError = uiState.categoriesError

    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showDraftDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.dateMillis
    )

    val handleBack: () -> Unit = {
        if (viewModel.hasUnsavedData() && !uiState.isSubmitting) {
            showDraftDialog = true
        } else {
            navController.popBackStack()
        }
    }

    // Intercept back navigation when form has unsaved data
    BackHandler(enabled = viewModel.hasUnsavedData() && !uiState.isSubmitting) {
        handleBack()
    }

    // Draft save dialog
    if (showDraftDialog) {
        AlertDialog(
            onDismissRequest = { showDraftDialog = false },
            title = { Text("¿Guardar como borrador?") },
            text = { Text("Puedes continuar editando esta solicitud más tarde desde la sección Borradores.") },
            confirmButton = {
                TextButton(onClick = {
                    showDraftDialog = false
                    viewModel.saveDraft()
                }) {
                    Text("Guardar borrador", color = VerviColors.Primary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Row {
                    TextButton(onClick = {
                        showDraftDialog = false
                        navController.popBackStack()
                    }) {
                        Text("Descartar", color = VerviColors.CancelRed)
                    }
                    TextButton(onClick = { showDraftDialog = false }) {
                        Text("Cancelar", color = VerviColors.TextGray)
                    }
                }
            }
        )
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is NewRequestEvent.Submitted -> navController.popBackStack()
                is NewRequestEvent.DraftSaved -> navController.popBackStack()
            }
        }
    }

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        viewModel.addAttachment(uri)
    }

    Scaffold(
        containerColor = VerviColors.BackgroundLight,
        topBar = {
            Column {
                VerviTopBar(
                    title = "Nueva Solicitud",
                    onBack = handleBack
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = VerviColors.BorderGray
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            VerviTextField(
                label = "TÍTULO DE LA TAREA",
                value = uiState.title,
                onValueChange = { viewModel.onTitleChange(it) },
                placeholder = "Ej: Resolver taller de cálculo vectorial",
                labelSize = 12.sp,
            )

            VerviTextArea(
                label = "DESCRIPCIÓN DETALLADA",
                value = uiState.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                placeholder = "Describe los entregables, temas específicos y cualquier instrucción adicional...",
                labelSize = 12.sp,
                minLines = 5,
                maxLines = 10
            )

            // Category — form-style dropdown
            Text(
                text = "CATEGORÍA",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = VerviColors.TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            ExposedDropdownMenuBox(
                expanded = categoryMenuExpanded,
                onExpandedChange = {
                    if (!uiState.isLoadingCategories && uiState.categoryOptions.isNotEmpty()) {
                        categoryMenuExpanded = it
                    }
                }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true),
                    value = uiState.category,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = {
                        Text(
                            if (uiState.isLoadingCategories) {
                                "Cargando categorias..."
                            } else {
                                "Selecciona una area academica"
                            },
                            color = Color(0xFFAAAAAA)
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryMenuExpanded)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = VerviColors.BorderGray,
                        focusedBorderColor = VerviColors.Blue,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )
                ExposedDropdownMenu(
                    expanded = categoryMenuExpanded,
                    onDismissRequest = { categoryMenuExpanded = false }
                ) {
                    uiState.categoryOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                viewModel.onCategorySelected(option)
                                categoryMenuExpanded = false
                            }
                        )
                    }
                }
            }

            if (categoriesError != null) {
                Text(
                    text = categoriesError,
                    color = Color(0xFFDC2626),
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            VerviTextField(
                label = "PRESUPUESTO ESTIMADO (COP)",
                value = uiState.budget,
                onValueChange = { viewModel.onBudgetChange(it) },
                placeholder = "$ 0.00",
                labelSize = 12.sp,
                keyboardType = KeyboardType.Decimal
            )

            // Required date
            Text(
                text = "FECHA REQUERIDA",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = VerviColors.TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
            ) {
                OutlinedTextField(
                    value = uiState.dateText,
                    onValueChange = {},
                    enabled = false,
                    placeholder = { Text("mm/dd/yyyy", color = Color(0xFFAAAAAA)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = VerviColors.TextGray,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = VerviColors.BorderGray,
                        disabledContainerColor = Color.White,
                        disabledTextColor = VerviColors.TextDark,
                        disabledTrailingIconColor = VerviColors.TextGray,
                        disabledPlaceholderColor = Color(0xFFAAAAAA),
                        unfocusedBorderColor = VerviColors.BorderGray,
                        focusedBorderColor = VerviColors.Blue,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )
            }

            // Optional attachments
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ARCHIVOS ADJUNTOS (OPCIONAL)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = VerviColors.TextSecondary
                )
                Text(
                    text = "Máx 3 imágenes",
                    fontSize = 12.sp,
                    color = VerviColors.TextGray
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                repeat(3) { index ->
                    AttachmentSlot(
                        uri = uiState.attachments.getOrNull(index),
                        onAddClick = {
                            pickImage.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        onRemoveClick = { viewModel.removeAttachment(index) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // CTA — send icon on the right (mockup)
            Button(
                onClick = {
                    if (!uiState.isSubmitting) {
                        viewModel.submit()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerviColors.Primary
                )
            ) {
                Text(
                    text = if (uiState.isSubmitting) "Publicando..." else "Publicar Solicitud",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            if (uiState.error != null) {
                Text(
                    text = uiState.error ?: "",
                    color = Color(0xFFDC2626),
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            VerviFooterText(
                text = "Al publicar, aceptas los términos de servicio de Vervi para la gestión académica en Colombia.",
                fontSize = 11.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            viewModel.onDateSelected(millis)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Aceptar", color = VerviColors.Primary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar", color = VerviColors.TextGray)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

