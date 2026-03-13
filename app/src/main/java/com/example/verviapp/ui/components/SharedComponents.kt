package com.example.verviapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.verviapp.ui.theme.VerviColors

// ════════════════════════════════════════════════════════════
//  VerviTopBar — barra superior con flecha atrás y título centrado
//  Uso: VerviTopBar(title = "Vervi", onBack = { finish() })
//       VerviTopBar(title = "Lista de perfiles", onBack = { navController.popBackStack() })
// ════════════════════════════════════════════════════════════
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerviTopBar(title: String, onBack: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = VerviColors.BgColor
        )
    )
}

// ════════════════════════════════════════════════════════════
//  VerviTextField — input de texto con label incluido
//  Uso: VerviTextField(label = "Correo", value = email, onValueChange = { email = it },
//           placeholder = "nombre@ejemplo.com", keyboardType = KeyboardType.Email)
//       VerviTextField(label = "Nombre", labelSize = 12.sp, value = nombre, ...)
// ════════════════════════════════════════════════════════════
@Composable
fun VerviTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    labelSize: TextUnit = 14.sp,                    // tamaño del label personalizable
    keyboardType: KeyboardType = KeyboardType.Text  // tipo de teclado: Text, Email, Number...
) {
    Text(label, fontWeight = FontWeight.Bold, fontSize = labelSize, color = VerviColors.TextDark)
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value           = value,
        onValueChange   = onValueChange,
        placeholder     = { Text(placeholder, color = Color(0xFFAAAAAA)) },
        singleLine      = true,
        shape           = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors          = verviFieldColors(),
        modifier        = modifier.fillMaxWidth()
    )
}

// ════════════════════════════════════════════════════════════
//  VerviPasswordField — input de contraseña con label y ojo
//  Uso: VerviPasswordField(label = "Contraseña", value = pass,
//           onValueChange = { pass = it }, visible = visible, onToggle = { visible = !visible })
// ════════════════════════════════════════════════════════════
@Composable
fun VerviPasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    visible: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    labelSize: TextUnit = 14.sp                     // tamaño del label personalizable
) {
    Text(label, fontWeight = FontWeight.Bold, fontSize = labelSize, color = VerviColors.TextDark)
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value         = value,
        onValueChange = onValueChange,
        placeholder   = { Text("••••••••", color = Color(0xFFAAAAAA)) },
        singleLine    = true,
        shape         = RoundedCornerShape(12.dp),
        // visualTransformation: oculta o muestra el texto según el estado
        visualTransformation = if (visible) VisualTransformation.None
        else PasswordVisualTransformation(),
        trailingIcon  = {
            IconButton(onClick = onToggle) {
                Icon(
                    imageVector        = if (visible) Icons.Default.VisibilityOff
                    else Icons.Default.Visibility,
                    contentDescription = "Mostrar/ocultar contraseña",
                    tint               = VerviColors.TextGray
                )
            }
        },
        colors   = verviFieldColors(),
        modifier = modifier.fillMaxWidth()
    )
}

// ════════════════════════════════════════════════════════════
//  VerviButton — botón principal de la app
//  Uso: VerviButton(text = "Ingresar →", onClick = { ... })
//       VerviButton(text = "Eliminar", onClick = { ... }, color = Color.Red)
// ════════════════════════════════════════════════════════════
@Composable
fun VerviButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = VerviColors.Blue     // azul por defecto, personalizable
) {
    Button(
        onClick  = onClick,
        shape    = RoundedCornerShape(12.dp),
        colors   = ButtonDefaults.buttonColors(containerColor = color),
        modifier = modifier.fillMaxWidth().height(52.dp)
    ) {
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

// ════════════════════════════════════════════════════════════
//  VerviFooterText — texto simple centrado para footer de pantallas
//
//  Uso: VerviFooterText("Conectando servicios en Colombia")
//       VerviFooterText("Versión 1.0.0", fontSize = 11.sp)
// ════════════════════════════════════════════════════════════
@Composable
fun VerviFooterText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 13.sp          // tamaño personalizable
) {
    Text(
        text      = text,
        fontSize  = fontSize,
        color     = VerviColors.TextGray,
        textAlign = TextAlign.Center,
        modifier  = modifier.fillMaxWidth()
    )
}

// ── verviFieldColors — colores estándar para todos los inputs ──
@Composable
private fun verviFieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedBorderColor    = Color.Transparent,    // sin borde al perder foco
    focusedBorderColor      = VerviColors.Blue,     // borde azul al enfocar
    unfocusedContainerColor = Color.White,
    focusedContainerColor   = Color.White
)