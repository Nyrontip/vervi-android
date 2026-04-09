package com.example.verviapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.verviapp.ui.components.*
import com.example.verviapp.ui.theme.VerviColors
import com.example.verviapp.viewmodel.AuthEvent
import com.example.verviapp.viewmodel.LoginViewModel
import com.example.verviapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {
    var selectedTab by remember { mutableStateOf(0) } // Tab seleccionado (0 es login, 1 registro)
    val state by viewModel.state.collectAsState()

    // Eventos one-shot para navegación.
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                AuthEvent.LoginSuccess,
                AuthEvent.RegisterSuccess -> {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        }
    }

    Scaffold(
        // VerviTopBar compartido — mismo estilo en toda la app, solo cambia el título
        topBar = { VerviTopBar(title = "Vervi", onBack = { navController.popBackStack()}) },
        containerColor = VerviColors.BgColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()) // scroll cuando el teclado sube
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Imagen hero — ContentScale.Crop recorta para llenar sin deformar
            Image(
                painter            = painterResource(id = R.drawable.login_hero),
                contentDescription = null,
                contentScale       = ContentScale.Crop,
                modifier           = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(20.dp))

            // TabRow de Material3 — maneja indicador, accesibilidad y animación solo
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor   = VerviColors.BgColor,
                contentColor     = VerviColors.Blue
            ) {
                listOf("Iniciar Sesión", "Registrarse").forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick  = { selectedTab = index },
                        text     = {
                            Text(
                                text       = title,
                                fontSize   = 15.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

                if (selectedTab == 0) {
                LoginForm(viewModel)
            } else {
                RegisterForm(viewModel)
            }

            // Mensaje de error debajo de los forms
            if (state.errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text     = state.errorMessage!!,
                    color    = androidx.compose.ui.graphics.Color.Red,
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            // Footer con link azul — no aplica como componente genérico por el estilo mixto
            // buildAnnotatedString: permite estilos distintos dentro de un mismo Text
            Text(
                text = buildAnnotatedString {
                    append("Al continuar, aceptas nuestros ")
                    withStyle(SpanStyle(color = VerviColors.Blue, fontWeight = FontWeight.Medium)) {
                        append("Términos y Condiciones")
                    }
                },
                fontSize  = 13.sp,
                color     = VerviColors.TextGray,
                textAlign = TextAlign.Center,
                modifier  = Modifier.fillMaxWidth()
            )
        }
    }
}

// ── LoginForm — STATELESS ──────────
@Composable
private fun LoginForm(viewModel: LoginViewModel) {
    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    VerviTextField(label = "Correo Electrónico", value = email, onValueChange = { email = it },
        placeholder = "nombre@ejemplo.com", keyboardType = KeyboardType.Email)

    Spacer(modifier = Modifier.height(20.dp))

    VerviPasswordField(label = "Contraseña", value = password, onValueChange = { password = it },
        visible = passwordVisible, onToggle = { passwordVisible = !passwordVisible})

    Spacer(modifier = Modifier.height(28.dp))
    VerviButton(text = "Ingresar  →", onClick = { viewModel.login(email, password) })
}

// ── RegisterForm — STATELESS: mismo patrón que LoginForm ───
@Composable
private fun RegisterForm(viewModel: LoginViewModel) {
    var nombre             by remember { mutableStateOf("") }
    var regEmail           by remember { mutableStateOf("") }
    var regPassword        by remember { mutableStateOf("") }
    var regConfirm         by remember { mutableStateOf("") }
    var regPasswordVisible by remember { mutableStateOf(false) }
    var regConfirmVisible  by remember { mutableStateOf(false) }

    VerviTextField(label = "Nombre de usuario", value = nombre, onValueChange = {nombre = it},
        placeholder = "Ejem: Camilo Martínez")

    Spacer(modifier = Modifier.height(20.dp))

    VerviTextField(label = "Correo Electrónico", value = regEmail, onValueChange = { regEmail = it },
        placeholder = "nombre@ejemplo.com", keyboardType = KeyboardType.Email)

    Spacer(modifier = Modifier.height(20.dp))

    VerviPasswordField(label = "Contraseña", value = regPassword, onValueChange = { regPassword = it },
        visible = regPasswordVisible, onToggle = {regPasswordVisible = !regPasswordVisible})

    Spacer(modifier = Modifier.height(20.dp))

    VerviPasswordField(label = "Confirmar contraseña", value = regConfirm,
        onValueChange = {regConfirm = it}, visible = regConfirmVisible, onToggle = {regConfirmVisible = !regConfirmVisible})

    Spacer(modifier = Modifier.height(28.dp))
    VerviButton(text = "Registrarse  →", onClick = {viewModel.register(nombre, regEmail, regPassword, regConfirm)})
}
