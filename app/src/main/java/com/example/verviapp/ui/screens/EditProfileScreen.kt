package com.example.verviapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.verviapp.R
import com.example.verviapp.ui.components.*
import com.example.verviapp.ui.theme.VerviColors
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.verviapp.viewmodel.EditProfileViewModel
import com.example.verviapp.viewmodel.ProfileViewModel

@Composable
fun EditProfileScreen(navController: NavController, viewModel: EditProfileViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    // Vuelve atrás al guardar exitosamente
    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            navController.popBackStack()
            viewModel.resetSaveSuccess()
        }
    }

    // Obtener el ProfileViewModel para leer el usuario actual
    val profileViewModel: ProfileViewModel = viewModel()
    val profileState by profileViewModel.state.collectAsState()
    // Inicializar con los datos reales al entrar a la pantalla
    LaunchedEffect(profileState.user) {
        if (profileState.user.id.isNotEmpty()) {
            viewModel.initWithUser(profileState.user)
        }
    }

    Scaffold(
        topBar         = { VerviTopBar(title = "Editar Perfil", onBack = { navController.popBackStack() }) },
        containerColor = VerviColors.BgColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // ── Foto circular + ícono de cámara superpuesto ─────
            Box(contentAlignment = Alignment.BottomEnd) {
                Image(
                    painter            = painterResource(id = R.drawable.login_hero),
                    contentDescription = "Foto de perfil",
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.White, CircleShape)
                )
                Box(
                    modifier         = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(VerviColors.Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Cambiar foto",
                        tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("CAMBIAR FOTO", fontSize = 12.sp, fontWeight = FontWeight.Bold,
                color = VerviColors.Primary, letterSpacing = 1.sp)

            Spacer(modifier = Modifier.height(24.dp))

            // ── Nombre completo ──────────────────────────────────
            VerviTextField(
                label         = "Nombre Completo",
                value         = state.name,
                onValueChange = { viewModel.onNameChange(it) },
                placeholder   = "Tu nombre completo"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Biografía — VerviTextArea multilínea ─────────────
            VerviTextArea(
                label         = "Biografía",
                value         = state.bio,
                onValueChange = { viewModel.onBioChange(it) },
                placeholder   = "Cuéntanos un poco sobre tus servicios..."
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Toggle modo prestador en card blanca ─────────────
            Card(
                shape    = RoundedCornerShape(12.dp),
                colors   = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Activar modo Prestador", fontSize = 14.sp,
                            fontWeight = FontWeight.Bold, color = VerviColors.TextDark)
                        Text("Habilita tu perfil para recibir solicitudes",
                            fontSize = 12.sp, color = VerviColors.TextSecondary)
                    }
                    Switch(
                        checked         = state.isProvider ,
                        onCheckedChange = { viewModel.onProviderToggle(it) },
                        colors          = SwitchDefaults.colors(
                            checkedThumbColor   = Color.White,
                            checkedTrackColor   = VerviColors.Primary,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = VerviColors.TextSecondary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Categorías con InputChip removible + FilterChip añadir ──
            // InputChip/FilterChip de Material3 — solo se usan aquí, no justifica componente global
            Text("Categorías", fontWeight = FontWeight.Bold, fontSize = 14.sp,
                color = VerviColors.TextDark, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier              = Modifier.fillMaxWidth()
            ) {
                state.categories.forEach { cat ->
                    InputChip(
                        selected     = true,
                        onClick      = { },
                        label        = { Text(cat, fontSize = 13.sp, fontWeight = FontWeight.Medium) },
                        // X elimina la categoría de la lista al tocar
                        trailingIcon = {
                            Icon(Icons.Default.Close, contentDescription = "Eliminar $cat",
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { viewModel.removeCategory(cat) })
                        },
                        colors = InputChipDefaults.inputChipColors(
                            selectedContainerColor    = VerviColors.Primary,
                            selectedLabelColor        = Color.White,
                            selectedTrailingIconColor = Color.White
                        ),
                        border = InputChipDefaults.inputChipBorder(
                            enabled             = true,
                            selected            = true,
                            borderColor         = Color.Transparent,
                            selectedBorderColor = Color.Transparent
                        )
                    )
                }
                FilterChip(
                    selected = false,
                    onClick  = { viewModel.addCategory("OTraa") },
                    label    = { Text("+ Añadir", fontSize = 13.sp) },
                    colors   = FilterChipDefaults.filterChipColors(
                        containerColor = Color.White,
                        labelColor     = VerviColors.TextDark
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled     = true,
                        selected    = false,
                        borderColor = VerviColors.BorderGray
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Precio y Ubicación en la misma fila ──────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    VerviTextField(
                        label         = "Precio (COP)",
                        value         = state.price,
                        onValueChange = { viewModel.onPriceChange(it) },
                        placeholder   = "50.000",
                        keyboardType  = androidx.compose.ui.text.input.KeyboardType.Number,
                        leadingIcon   = Icons.Default.AttachMoney
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    VerviTextField(
                        label         = "Ubicación",
                        value         = state.location,
                        onValueChange = { viewModel.onLocationChange(it) },
                        placeholder   = "Bogotá",
                        leadingIcon   = Icons.Default.LocationOn
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── Guardar con ícono ────────────────────────────────
            VerviButton(
                text    = "Guardar Cambios",
                onClick = { viewModel.save() },
                icon    = Icons.Default.Save
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ── Cancelar — outlined sin color destructivo ────────
            VerviOutlinedButton(
                text    = "Cancelar",
                onClick = { navController.popBackStack() },
                color   = VerviColors.TextSecondary,
                height  = 48.dp
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}