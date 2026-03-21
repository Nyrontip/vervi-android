package com.example.verviapp.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
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
import com.example.verviapp.ui.components.VerviFooterText
import com.example.verviapp.ui.components.VerviTextArea
import com.example.verviapp.ui.components.VerviTextField
import com.example.verviapp.ui.components.VerviTopBar
import com.example.verviapp.ui.theme.VerviColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * New request form screen (mockup: form + attachments + CTA).
 * Uses [VerviTopBar], [VerviTextField], [VerviTextArea], [VerviFooterText].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRequestScreen(navController: NavController) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var dateText by remember { mutableStateOf("") }
    var dateMillis by remember { mutableStateOf<Long?>(null) }

    var categoryMenuExpanded by remember { mutableStateOf(false) }
    val categoryOptions = remember {
        listOf(
            "Matemáticas y estadística",
            "Ciencias e ingeniería",
            "Humanidades y letras",
            "Idiomas",
            "Economía y administración",
            "Arte y diseño"
        )
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = dateMillis
    )

    val dateFormat = remember {
        SimpleDateFormat("MM/dd/yyyy", Locale.US)
    }

    /** Up to 3 images; fixed slot indices. */
    var attachments by remember { mutableStateOf<List<Uri?>>(listOf(null, null, null)) }

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        val firstEmptyIndex = attachments.indexOfFirst { it == null }
        if (firstEmptyIndex >= 0) {
            val next = attachments.toMutableList()
            next[firstEmptyIndex] = uri
            attachments = next
        }
    }

    Scaffold(
        containerColor = VerviColors.BackgroundLight,
        topBar = {
            Column {
                VerviTopBar(
                    title = "Nueva Solicitud",
                    onBack = { navController.popBackStack() }
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
                value = title,
                onValueChange = { title = it },
                placeholder = "Ej: Resolver taller de cálculo vectorial",
                labelSize = 12.sp,
            )

            VerviTextArea(
                label = "DESCRIPCIÓN DETALLADA",
                value = description,
                onValueChange = { description = it },
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
                onExpandedChange = { categoryMenuExpanded = it }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true),
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = {
                        Text(
                            "Selecciona una área académica",
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
                    categoryOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                category = option
                                categoryMenuExpanded = false
                            }
                        )
                    }
                }
            }

            VerviTextField(
                label = "PRESUPUESTO ESTIMADO (COP)",
                value = budget,
                onValueChange = { budget = it },
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
            OutlinedTextField(
                value = dateText,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("mm/dd/yyyy", color = Color(0xFFAAAAAA)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
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
                    unfocusedBorderColor = VerviColors.BorderGray,
                    focusedBorderColor = VerviColors.Blue,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

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
                        uri = attachments.getOrNull(index),
                        onAddClick = {
                            pickImage.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        onRemoveClick = {
                            val next = attachments.toMutableList()
                            if (index < next.size) next[index] = null
                            attachments = next
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // CTA — send icon on the right (mockup)
            Button(
                onClick = { /* TODO: submit to backend */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerviColors.Primary
                )
            ) {
                Text(
                    text = "Publicar Solicitud",
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
                            dateMillis = millis
                            dateText = dateFormat.format(Date(millis))
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

@Composable
private fun AttachmentSlot(
    uri: Uri?,
    onAddClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(10.dp)
    val slotBorder = BorderStroke(1.dp, VerviColors.BorderGray)

    Box(
        modifier = modifier
            .height(96.dp)
            .clip(shape)
            .border(slotBorder, shape)
            .background(
                if (uri != null) Color(0xFFF5F0E8) else Color(0xFFE8EEF4)
            )
            .clickable(enabled = uri == null) { onAddClick() },
        contentAlignment = Alignment.Center
    ) {
        if (uri != null) {
            AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE53935))
                    .clickable { onRemoveClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Remove",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.AddPhotoAlternate,
                    contentDescription = null,
                    tint = VerviColors.Primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Agregar",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = VerviColors.Primary
                )
            }
        }
    }
}
