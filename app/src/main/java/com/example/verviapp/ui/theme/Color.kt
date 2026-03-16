package com.example.verviapp.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// ── Colores propios de Vervi ──────────────────────────────────
object VerviColors {
    // ---------- PRIMARY ----------
    val Primary = Color(0xFF32619F)             // azul principal
    val Blue = Color(0xFF2D6BE4)               // otro azul usado en algunas pantallas
    val OrangeSecondary = Color(0xFFFE993D)    // naranja secundario / FAB

    // ---------- BACKGROUNDS ----------
    val BackgroundLight = Color(0xFFF6F7F8)
    val BackgroundDark = Color(0xFF14181E)
    val CardBackground = Color.White
    val BottomSheetBackground = Color.White
    val CommentInputBackground = Color(0xFFF8FAFC)
    val CommentBackground = Color(0xFFF1F5F9)
    val NotificationBackgroundLight = Color(0xFFF1F5F9)
    val BgColor = Color(0xFFF2F4F8)           // fondo general de algunas pantallas
    val Overlay = Color.Black.copy(alpha = 0.4f)
    val BackgroundOther = Color.White          // fondo de mensaje del otro

    // ---------- TEXT ----------
    val TextPrimary = Color(0xFF1F2937)       // texto principal oscuro
    val TextSecondary = Color(0xFF6B7280)     // texto secundario gris
    val TextGray = Color.Gray
    val TextMid = Color(0xFF555555)           // texto intermedio
    val TextDark = Color(0xFF1A1A1A)
    val TextWhite = Color.White
    val TextUser = Color.White                 // texto dentro del mensaje del usuario
    val TextOther = Color(0xFF1E293B)         // texto dentro del mensaje del otro)

    // ---------- ICONS ----------
    val IconGray = Color.Gray
    val IconLightGray = Color.LightGray

    // ---------- STARS ----------
    val StarFilled = Color(0xFFFFC107)        // amarillo de estrellas
    val StarOrange = Color(0xFFFF9800)
    val StarEmpty = Color.LightGray

    // ---------- STATUS / DOTS ----------
    val StatusSuccess = Color(0xFF10B981)     // en curso
    val StatusPending = Color(0xFFF59E0B)     // pendiente
    val StatusDraft = Color.Gray               // borrador
    val StatusOnline = Color.Green
    val StatusBorder = Color.White

    // ---------- BORDERS / DIVIDERS ----------
    val BorderGray = Color(0xFFE5E7EB)
    val Divider = Color(0xFFE5E7EB)

    // ---------- NOTIFICATIONS ----------
    val NotificationUnread = Primary.copy(alpha = 1f)
    val NotificationBackgroundAlpha = Primary.copy(alpha = 0.1f)

    // ---------- FLOATING ACTION BUTTON ----------
    val FabOrange = OrangeSecondary

    // ---------- TRANSPARENT ----------
    val Transparent = Color.Transparent

    // ---------- PROGRESS / LOADING ----------
    val ProgressBg = Color(0xFFE0E0E0)
}