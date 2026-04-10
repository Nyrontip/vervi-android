package com.example.verviapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.navigation.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.verviapp.viewmodel.state.RequestItem
import com.example.verviapp.ui.theme.VerviColors
import com.example.verviapp.viewmodel.state.ServiceHistoryItem

// ════════════════════════════════════════════════════════════
//  VerviTopBar — barra superior con título centrado
//
//  onBack = null  → sin flecha (ej: Home)
//  onBack = { }   → con flecha de volver (ej: Login, Detalle)
//  actions        → iconos opcionales a la derecha (ej: perfil, notificaciones)
//
//  Uso sin flecha con acciones:
//    VerviTopBar(title = "Vervi", actions = {
//        IconButton(onClick = {}) { Icon(Icons.Default.Person, null) }
//    })
//  Uso con flecha sin acciones:
//    VerviTopBar(title = "Detalle", onBack = { finish() })
// ════════════════════════════════════════════════════════════
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerviTopBar(
    title: String,
    onBack: (() -> Unit)? = null,                           // null = sin flecha
    actions: @Composable RowScope.() -> Unit = {},          // iconos derechos opcionales
    barContainerColor: Color = VerviColors.BgColor           // fondo de la barra (pantallas con otro tono)
) {
    CenterAlignedTopAppBar(
        title = { Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
        navigationIcon = {
            // Solo muestra la flecha si onBack no es null
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = barContainerColor
        )
    )
}

// ════════════════════════════════════════════════════════════
//  VerviBottomBar — barra de navegación inferior global
//  Items fijos de la app — quemados aquí para no repetirlos en cada pantalla.
// ════════════════════════════════════════════════════════════
private data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)
@Composable
fun VerviBottomBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(
            "Inicio",
            Icons.Default.Home,
            "home"
        ),
        BottomNavItem(
            "Solicitudes",
            Icons.Default.ListAlt,
            "requests/management"
        ),
        BottomNavItem(
            "Historial",
            Icons.Default.History,
            "services/history"
        ),
        BottomNavItem(
            "Perfil",
            Icons.Default.Person,
            "profile"
        )
    )

// Observa el backStack del NavController
    val navBackStackEntry by navController.currentBackStackEntryAsState()

// Obtiene la ruta de la pantalla actual
    val currentRoute = navBackStackEntry?.destination?.route

// Busca el índice del item que coincide con la ruta actual
    val selectedIndex = items.indexOfFirst {
        it.route.lowercase() == currentRoute?.lowercase()  // lowercase moderno y seguro con null
    }.takeIf { it >= 0 } ?: 0  // fallback a Inicio si no coincide ninguno

    NavigationBar(
            containerColor = Color.White,
            tonalElevation = 0.dp
    ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected  = selectedIndex == index,
                    onClick = {
                        val route = items.get(index).route
                        if (index == selectedIndex || route == "") {// No navega si ya está en ese Activity o no hay ruta
                            return@NavigationBarItem
                        }
                        navController.navigate(route) {
                        // Configuraciones equivalentes al FLAG
                            launchSingleTop = true      // Evita duplicar la pantalla en el backstack
                            restoreState = true         // Trae la pantalla del backstack si ya existe
                        }
                    },
                    icon      = { Icon(item.icon, contentDescription = item.label) },
                    label     = { Text(item.label, fontSize = 11.sp) },
                    alwaysShowLabel = true,
                    colors    = NavigationBarItemDefaults.colors(
                        selectedIconColor   = VerviColors.Blue,
                        selectedTextColor   = VerviColors.Blue,
                        unselectedIconColor = VerviColors.TextGray,
                        unselectedTextColor = VerviColors.TextGray,
                        indicatorColor      = Color.Transparent
                    )
                )
            }
        }
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
    labelSize: TextUnit = 14.sp,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: ImageVector? = null,         // ícono izquierdo opcional
    labelToFieldSpacing: Dp = 4.dp             // espacio label → campo (antes 8.dp)
) {
    Text(label, fontWeight = FontWeight.Bold, fontSize = labelSize, color = VerviColors.TextDark)
    Spacer(modifier = Modifier.height(labelToFieldSpacing))
    OutlinedTextField(
        value           = value,
        onValueChange   = onValueChange,
        placeholder     = { Text(placeholder, color = Color(0xFFAAAAAA)) },
        singleLine      = true,
        shape           = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        leadingIcon     = if (leadingIcon != null) ({
            Icon(leadingIcon, contentDescription = null,
                tint = Color(0xFFAAAAAA), modifier = Modifier.size(18.dp))
        }) else null,
        colors          = verviFieldColors(),
        modifier        = modifier.fillMaxWidth()
    )
}

// ════════════════════════════════════════════════════════════
//  VerviTextArea — input de texto multilínea con label
//  Uso: VerviTextArea(label = "Biografía", value = bio,
//           onValueChange = { bio = it }, placeholder = "Cuéntanos...")
//       VerviTextArea(..., minLines = 6)
// ════════════════════════════════════════════════════════════
@Composable
fun VerviTextArea(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    labelSize: TextUnit = 14.sp,
    minLines: Int = 4,
    maxLines: Int = 8,
    labelToFieldSpacing: Dp = 4.dp
) {
    Text(label, fontWeight = FontWeight.Bold, fontSize = labelSize, color = VerviColors.TextDark)
    Spacer(modifier = Modifier.height(labelToFieldSpacing))
    OutlinedTextField(
        value         = value,
        onValueChange = onValueChange,
        placeholder   = { Text(placeholder, color = Color(0xFFAAAAAA)) },
        shape         = RoundedCornerShape(12.dp),
        minLines      = minLines,
        maxLines      = maxLines,
        colors        = verviFieldColors(),
        modifier      = modifier.fillMaxWidth()
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
    labelSize: TextUnit = 14.sp,                    // tamaño del label personalizable
    labelToFieldSpacing: Dp = 4.dp
) {
    Text(label, fontWeight = FontWeight.Bold, fontSize = labelSize, color = VerviColors.TextDark)
    Spacer(modifier = Modifier.height(labelToFieldSpacing))
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
//  VerviSearchField — barra de búsqueda estilizada
//  Uso: VerviSearchField(value = query, onValueChange = { query = it },
//           placeholder = "¿Qué servicio necesitas?")
// ════════════════════════════════════════════════════════════
@Composable
fun VerviSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Buscar...",
    modifier: Modifier = Modifier,
    onSearchClick: (() -> Unit)? = null
) {
    OutlinedTextField(
        value           = value,
        onValueChange   = onValueChange,
        placeholder     = { Text(placeholder, color = Color(0xFFAAAAAA), fontSize = 14.sp) },
        singleLine      = true,
        shape           = RoundedCornerShape(16.dp),                    // más redondeado que inputs de form
        trailingIcon    = if (onSearchClick != null) ({
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = VerviColors.Blue,
                    modifier = Modifier.size(20.dp)
                )
            }
        }) else null,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        colors          = verviFieldColors(),
        textStyle     = LocalTextStyle.current.copy(fontSize = 14.sp),
        modifier      = modifier
            .fillMaxWidth()
            .height(52.dp)   // altura fija compacta
    )
}

// ════════════════════════════════════════════════════════════
//  VerviChips — fila de chips seleccionables (solo uno activo a la vez)
//  Uso: VerviChips(opciones = listOf("Todos","Limpieza"), selected = cat,
//           onSelect = { cat = it })
// ════════════════════════════════════════════════════════════
@Composable
fun VerviChips(
    opciones: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier              = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        opciones.forEach { opcion ->
            val isSelected = opcion == selected
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) VerviColors.Blue else Color.White)
                    .clickable { onSelect(opcion) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text       = opcion,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color      = if (isSelected) Color.White else VerviColors.TextDark
                )
            }
        }
    }
}

// ════════════════════════════════════════════════════════════
//  VerviBadge — chip/badge informativo NO seleccionable
//
//  Dos variantes:
//  - filled:   fondo de color, texto blanco  (ej: precio "$50.000 COP")
//  - outlined: fondo transparente, borde+texto de color  (ej: "URGENTE")
//
//  Uso filled:   VerviBadge(text = "$50.000 COP", color = VerviColors.Blue)
//  Uso outlined: VerviBadge(text = "URGENTE", color = VerviColors.Blue, outlined = true)
// ════════════════════════════════════════════════════════════
@Composable
fun VerviBadge(
    text: String,
    color: Color = VerviColors.Blue,
    outlined: Boolean = false,              // false = fondo de color | true = solo borde
    fontSize: TextUnit = 11.sp
) {
    val bgColor   = if (outlined) Color.Transparent else Color.White
    val borderMod = if (outlined)
        Modifier.border(1.dp, color, RoundedCornerShape(6.dp))
    else
        Modifier

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .then(borderMod)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text, fontSize = fontSize, fontWeight = FontWeight.Bold, color = color)
    }
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
    color: Color = VerviColors.Blue,
    fillWidth: Boolean = true,
    height: Dp = 52.dp,
    fontSize: TextUnit = 16.sp,
    icon: ImageVector? = null             // ícono izquierdo opcional
) {
    Button(
        onClick  = onClick,
        shape    = RoundedCornerShape(12.dp),
        colors   = ButtonDefaults.buttonColors(containerColor = color),
        modifier = modifier
            .then(if (fillWidth) Modifier.fillMaxWidth() else Modifier)
            .height(height)
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(text, fontSize = fontSize, fontWeight = FontWeight.Bold)
    }
}

// ════════════════════════════════════════════════════════════
//  VerviOutlinedButton — botón con borde, sin relleno
//  Uso: VerviOutlinedButton(text = "Ver Perfil", onClick = { ... })
//       VerviOutlinedButton(text = "Cancelar", color = Color.Red, height = 40.dp)
// ════════════════════════════════════════════════════════════
@Composable
fun VerviOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = VerviColors.Blue,  // color del borde y texto
    height: Dp = 46.dp,
    fontSize: TextUnit = 14.sp
) {
    OutlinedButton(
        onClick  = onClick,
        shape    = RoundedCornerShape(10.dp),
        border   = BorderStroke(1.dp, color),
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        Text(text, fontSize = fontSize, fontWeight = FontWeight.Bold, color = color)
    }
}

// ════════════════════════════════════════════════════════════
//  VerviActivityItem — fila de actividad con ícono, textos y flecha
//  Uso:
//    VerviActivityItem(
//        icon      = Icons.Default.ListAlt,
//        titulo    = "Mis Solicitudes",
//        subtitulo = "Ver tus pedidos pendientes",
//        onClick   = { }
//    )
//    // Color del ícono personalizable:
//    VerviActivityItem(..., iconColor = VerviColors.OrangeSecondary)
// ════════════════════════════════════════════════════════════
@Composable
fun VerviActivityItem(
    icon: ImageVector,
    titulo: String,
    subtitulo: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconColor: Color = VerviColors.Blue         // color del ícono personalizable
) {
    Row(
        modifier          = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícono con fondo redondeado azul suave
        Box(
            modifier          = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(VerviColors.BgColor),
            contentAlignment  = Alignment.Center
        ) {
            Icon(icon, contentDescription = null,
                tint = iconColor, modifier = Modifier.size(22.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Textos ocupan el espacio restante
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, fontSize = 14.sp, fontWeight = FontWeight.Bold,
                color = VerviColors.TextDark)
            Text(subtitulo, fontSize = 12.sp, color = VerviColors.TextGray)
        }

        // Flecha de navegación
        Icon(Icons.Default.ChevronRight, contentDescription = null,
            tint = VerviColors.TextGray, modifier = Modifier.size(20.dp))
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

// ════════════════════════════════════════════════════════════
//  VerviSmallButton — botón compacto para cards y acciones secundarias
//
//  Variante ligera de VerviButton, optimizada para espacios reducidos
//  como tarjetas, listas o acciones inline.
//
//  - Usa fondo con alpha (no sólido)
//  - Altura reducida
//  - Texto más pequeño
//
//  Uso:
//    VerviSmallButton(
//        text = "Cancelar",
//        color = VerviColors.CancelRed,
//        onClick = { }
//    )
//
//    VerviSmallButton(
//        text = "Confirmar",
//        color = VerviColors.Primary,
//        onClick = { },
//        modifier = Modifier.weight(1f)
//    )
// ════════════════════════════════════════════════════════════
@Composable
fun VerviSmallButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = VerviColors.Blue,
    backgroundAlpha: Float = 0.1f,
    height: Dp = 36.dp,
    fontSize: TextUnit = 12.sp
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color.copy(alpha = backgroundAlpha)
        ),
        contentPadding = PaddingValues(vertical = 6.dp),
        modifier = modifier
            .then(if (modifier == Modifier) Modifier.fillMaxWidth() else Modifier)
            .height(height)
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

// ════════════════════════════════════════════════════════════
//  VerviStatusBadge — indicador de estado con punto + texto
//
//  Reemplaza combinaciones manuales como:
//  StatusDot + TextSmall
//
//  Incluye:
//  - Punto de color (estado visual)
//  - Texto en mayúscula
//  - Fondo suave con alpha
//
//  Ideal para estados como:
//  "En curso", "Pendiente", "Borrador"
//
//  Uso:
//    VerviStatusBadge(
//        text = "En curso",
//        color = Color(0xFF10B981)
//    )
//
//    VerviStatusBadge(
//        text = request.status,
//        color = request.statusColor
//    )
// ════════════════════════════════════════════════════════════
@Composable
fun VerviStatusBadge(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(color, CircleShape)
        )

        Text(
            text = text.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

// ════════════════════════════════════════════════════════════
//  VerviRequestCard — tarjeta reutilizable de solicitud
//
//  Representa una solicitud del usuario con:
//  - Estado (badge)
//  - Título
//  - Fecha + postulaciones
//  - Imagen
//  - Acciones (botones dinámicos)
//
//  Lógica:
//  - "Borrador" → botón "Eliminar"
//  - Otros estados → "Cancelar" + "Confirmar"
//
//  Diseñada para ser reutilizada en:
//  - RequestsScreen
//  - Home
//  - Historial
//
//  Uso:
//    VerviRequestCard(
//        navController = navController,
//        request = requestItem
//    )
// ════════════════════════════════════════════════════════════
@Composable
fun VerviRequestCard(
    navController: NavController,
    request: com.example.verviapp.viewmodel.state.RequestItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(VerviColors.CardBackground, RoundedCornerShape(12.dp))
            .border(1.dp, VerviColors.BorderGray, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {

                VerviStatusBadge(
                    text = request.status,
                    color = request.statusColor
                )

                Text(
                    text = request.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerviColors.TextPrimary
                )

                Text(
                    text = "${request.date} • ${request.applications}",
                    fontSize = 12.sp,
                    color = VerviColors.Primary
                )
            }

            Row(
                modifier = Modifier.padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                if (request.status == "Borrador") {
                    VerviSmallButton(
                        text = "Eliminar",
                        color = VerviColors.CancelRed,
                        onClick = { navController.navigate("request/details") },
                        modifier = Modifier.weight(1f)
                    )
                } else {

                    VerviSmallButton(
                        text = "Cancelar",
                        color = VerviColors.CancelRed,
                        onClick = { navController.navigate("request/cancel") },
                        modifier = Modifier.weight(1f)
                    )

                    VerviSmallButton(
                        text = "Confirmar",
                        color = VerviColors.Primary,
                        onClick = { navController.navigate("request/confirm") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        AsyncImage(
            model = request.imageUrl,
            contentDescription = request.title,
            contentScale = ContentScale.Crop,

            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    }
}

// ════════════════════════════════════════════════════════════
//  VerviServiceHistoryCard — tarjeta de historial de servicios
//
//  Representa un servicio ya realizado con:
//  - Imagen
//  - Título
//  - Proveedor + fecha
//  - Precio (badge)
//  - Estado de calificación (rating o "SIN CALIFICAR")
//  - Acción (ver detalles / calificar)
//
//  Lógica:
//  - Si rating != null → muestra estrellas + "Ver detalles"
//  - Si rating == null → muestra badge "SIN CALIFICAR" + "Calificar ahora"
//
//  Diseñada para ser reutilizada en:
//  - ServiceHistoryScreen
//  - Perfil (historial)
//  - Dashboard usuario
//
//  Uso:
//    VerviServiceHistoryCard(
//        item = service,
//        onClick = { },
//        onRate = { }
//    )
// ════════════════════════════════════════════════════════════
@Composable
fun VerviServiceHistoryCard(
    item: ServiceHistoryItem,
    onClick: () -> Unit,
    onRate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .background(VerviColors.CardBackground, RoundedCornerShape(12.dp))
            .border(1.dp, VerviColors.BorderGray, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {

        Row(horizontalArrangement = Arrangement.SpaceBetween) {

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = VerviColors.TextDark
                )

                Text(
                    text = "${item.provider} • ${item.date}",
                    fontSize = 12.sp,
                    color = VerviColors.TextGray
                )

                VerviBadge(
                    text = item.price,
                    color = VerviColors.Blue
                )
            }

            Spacer(Modifier.width(12.dp))

            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }

        Spacer(Modifier.height(12.dp))

        Divider()

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (item.rating != null) {
                VerviRatingStars(item.rating)
            } else {
                VerviBadge(
                    text = "SIN CALIFICAR",
                    color = VerviColors.TextGray,
                    outlined = true
                )
            }

            Row(
                modifier = Modifier.clickable {
                    if (item.rating != null) onClick() else onRate()
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (item.rating != null) "Ver detalles" else "Calificar ahora",
                    color = VerviColors.Blue,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )

                Spacer(Modifier.width(4.dp))

                Icon(
                    imageVector =
                        if (item.rating != null)
                            Icons.Default.ChevronRight
                        else
                            Icons.Default.Star,
                    contentDescription = null,
                    tint = VerviColors.Blue,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

// ════════════════════════════════════════════════════════════
//  VerviRatingStars — visualización de calificación con estrellas
//
//  Muestra:
//  - 5 estrellas (rellenas según rating)
//  - Valor numérico al lado
//
//  Uso:
//    VerviRatingStars(4.5f)
//    VerviRatingStars(rating = item.rating)
//
//  Nota:
//  - Soporta valores decimales (visual simplificado por ahora)
// ════════════════════════════════════════════════════════════
@Composable
fun VerviRatingStars(
    rating: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        repeat(5) { index ->
            val filled = index < rating.toInt()

            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (filled) Color(0xFFFFC107) else Color.LightGray,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(Modifier.width(4.dp))

        Text(
            text = rating.toString(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = VerviColors.TextDark
        )
    }
}

// ════════════════════════════════════════════════════════════
//  VerviTabs — tabs reutilizables de la app
//
//  Basado en TabRow de Material3, estilizado con VerviColors
//
//  Uso:
//    VerviTabs(
//        tabs = listOf("Activas", "Finalizadas"),
//        selectedIndex = selected,
//        onTabSelected = { selected = it }
//    )
//
//    VerviTabs(
//        tabs = listOf("Como cliente", "Como prestador"),
//        selectedIndex = selected,
//        onTabSelected = { selected = it }
//    )
// ════════════════════════════════════════════════════════════
@Composable
fun VerviTabs(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = VerviColors.BgColor,
        contentColor = VerviColors.Blue,
        modifier = modifier
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = if (selectedIndex == index)
                            FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }
    }
}
