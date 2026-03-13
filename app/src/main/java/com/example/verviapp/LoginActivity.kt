package com.example.verviapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import com.example.verviapp.ui.components.*
import com.example.verviapp.ui.theme.VerviAppTheme
import com.example.verviapp.ui.theme.VerviColors

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { VerviAppTheme  { LoginScreen(onBack = { finish() }) } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onBack: () -> Unit = {}) {
    var selectedTab by remember { mutableStateOf(0) }

    // Estado login
    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Estado registro
    var nombre             by remember { mutableStateOf("") }
    var regEmail           by remember { mutableStateOf("") }
    var regPassword        by remember { mutableStateOf("") }
    var regConfirm         by remember { mutableStateOf("") }
    var regPasswordVisible by remember { mutableStateOf(false) }
    var regConfirmVisible  by remember { mutableStateOf(false) }

    Scaffold(
        // VerviTopBar compartido — mismo estilo en toda la app, solo cambia el título
        topBar = { VerviTopBar(title = "Vervi", onBack = onBack) },
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
                LoginForm(
                    email            = email,
                    password         = password,
                    passwordVisible  = passwordVisible,
                    onEmailChange    = { email = it },
                    onPasswordChange = { password = it },
                    onTogglePassword = { passwordVisible = !passwordVisible },
                    onLogin          = { /* TODO: lógica de login */ }
                )
            } else {
                RegisterForm(
                    nombre           = nombre,
                    email            = regEmail,
                    password         = regPassword,
                    confirmPassword  = regConfirm,
                    passwordVisible  = regPasswordVisible,
                    confirmVisible   = regConfirmVisible,
                    onNombreChange   = { nombre = it },
                    onEmailChange    = { regEmail = it },
                    onPasswordChange = { regPassword = it },
                    onConfirmChange  = { regConfirm = it },
                    onTogglePassword = { regPasswordVisible = !regPasswordVisible },
                    onToggleConfirm  = { regConfirmVisible = !regConfirmVisible },
                    onRegister       = { /* TODO: lógica de registro */ }
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
private fun LoginForm(
    email: String,
    password: String,
    passwordVisible: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePassword: () -> Unit,
    onLogin: () -> Unit
) {
    VerviTextField(label = "Correo Electrónico", value = email, onValueChange = onEmailChange,
        placeholder = "nombre@ejemplo.com", keyboardType = KeyboardType.Email)

    Spacer(modifier = Modifier.height(20.dp))

    VerviPasswordField(label = "Contraseña", value = password, onValueChange = onPasswordChange,
        visible = passwordVisible, onToggle = onTogglePassword)

    Spacer(modifier = Modifier.height(28.dp))
    VerviButton(text = "Ingresar  →", onClick = onLogin)
}

// ── RegisterForm — STATELESS: mismo patrón que LoginForm ───
@Composable
private fun RegisterForm(
    nombre: String,
    email: String,
    password: String,
    confirmPassword: String,
    passwordVisible: Boolean,
    confirmVisible: Boolean,
    onNombreChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmChange: (String) -> Unit,
    onTogglePassword: () -> Unit,
    onToggleConfirm: () -> Unit,
    onRegister: () -> Unit
) {
    VerviTextField(label = "Nombre de usuario", value = nombre, onValueChange = onNombreChange,
        placeholder = "Ejem: Camilo Martínez")

    Spacer(modifier = Modifier.height(20.dp))

    VerviTextField(label = "Correo Electrónico", value = email, onValueChange = onEmailChange,
        placeholder = "nombre@ejemplo.com", keyboardType = KeyboardType.Email)

    Spacer(modifier = Modifier.height(20.dp))

    VerviPasswordField(label = "Contraseña", value = password, onValueChange = onPasswordChange,
        visible = passwordVisible, onToggle = onTogglePassword)

    Spacer(modifier = Modifier.height(20.dp))

    VerviPasswordField(label = "Confirmar contraseña", value = confirmPassword,
        onValueChange = onConfirmChange, visible = confirmVisible, onToggle = onToggleConfirm)

    Spacer(modifier = Modifier.height(28.dp))
    VerviButton(text = "Registrarse  →", onClick = onRegister)
}