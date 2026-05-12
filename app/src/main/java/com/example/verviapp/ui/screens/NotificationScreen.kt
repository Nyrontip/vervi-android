package com.example.verviapp.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.example.verviapp.ui.components.VerviSmallButton
import com.example.verviapp.ui.theme.VerviColors
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.verviapp.viewmodel.NotificationsViewModel
import com.example.verviapp.viewmodel.state.NotificationType

@Composable
fun VerviNotificationCard(
    title: String,
    description: String,
    time: String,
    type: NotificationType,
    unread: Boolean = false,
    onClick: () -> Unit
) {

    // Icono y colores según tipo de notificación
    val icon = when (type) {
        NotificationType.APPLICATION -> Icons.Default.Notifications
        NotificationType.MESSAGE -> Icons.Default.ChatBubble
        NotificationType.CONFIRMED -> Icons.Default.CheckCircle
        NotificationType.PAYMENT -> Icons.Default.Payments
        NotificationType.REMINDER -> Icons.Default.Event
    }

    val background = when (type) {
        NotificationType.APPLICATION, NotificationType.CONFIRMED -> VerviColors.NotificationBackgroundAlpha
        else -> VerviColors.NotificationBackgroundLight
    }

    val tint = when (type) {
        NotificationType.APPLICATION, NotificationType.CONFIRMED -> VerviColors.Primary
        else -> VerviColors.IconGray
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(VerviColors.CardBackground)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {

        // Icono de notificación
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(background),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = tint)

            if (unread) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = -2.dp, y = (2).dp)
                        .size(10.dp)
                        .background(VerviColors.OrangeSecondary, CircleShape)
                        .border(2.dp, VerviColors.TextWhite, CircleShape)
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        // Contenido
        Column(modifier = Modifier.weight(1f)) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = VerviColors.TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    time,
                    fontSize = 11.sp,
                    color = VerviColors.TextSecondary
                )
            }

            Spacer(Modifier.height(4.dp))

            Text(
                description,
                fontSize = 12.sp,
                color = VerviColors.TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/* ------------------------------------------------ */
/* SCREEN: NotificationsScreen */
/* ------------------------------------------------ */
@Suppress("DEPRECATION")
@Composable
fun NotificationsScreen(
    navController: NavController,
    viewModel: NotificationsViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            com.example.verviapp.ui.components.VerviTopBar(
                title = "Notificaciones",
                onBack = { navController.popBackStack() }
            )
        },
        bottomBar = {
            com.example.verviapp.ui.components.VerviBottomBar(
                navController
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(VerviColors.BgColor)
        ) {

            // Tabs compartidos
            com.example.verviapp.ui.components.VerviTabs(
                tabs = listOf("Todas", "No Leídas"),
                selectedIndex = uiState.selectedTab,
                onTabSelected = { viewModel.selectTab(it) }
            )

            when {
                // ── Cargando ─────────────────────────────────
                uiState.isLoading -> {
                    NotificationLoadingContent(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // ── Error ───────────────────────────────────
                uiState.error != null -> {
                    NotificationErrorContent(
                        message = uiState.error!!,
                        onRetry = { viewModel.retry() },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // ── Vacío ───────────────────────────────────
                uiState.notifications.isEmpty() -> {
                    NotificationEmptyContent(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // ── Datos ───────────────────────────────────
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        items(uiState.notifications) { item ->
                            VerviNotificationCard(
                                title = item.title,
                                description = item.description,
                                time = item.time,
                                type = item.type,
                                unread = item.unread,
                                onClick = {
                                    if (item.unread) viewModel.markAsRead(item)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/* ------------------------------------------------ */
/* Loading state — shimmer placeholder cards       */
/* ------------------------------------------------ */
@Composable
private fun NotificationLoadingContent(modifier: Modifier = Modifier) {
    val shimmerColors = listOf(
        VerviColors.BorderGray.copy(alpha = 0.3f),
        VerviColors.BorderGray.copy(alpha = 0.7f),
        VerviColors.BorderGray.copy(alpha = 0.3f)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerOffset"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        items(6) {
            ShimmerCard(brush)
        }
    }
}

@Composable
private fun ShimmerCard(brush: Brush) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(VerviColors.CardBackground)
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Placeholder del ícono — misma altura que el real (48dp)
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(brush)
        )
        Spacer(Modifier.width(12.dp))

        // Columna con textos — misma estructura que VerviNotificationCard
        Column(modifier = Modifier.weight(1f)) {
            // Fila título + hora
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Título — placeholder compacto
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
                Spacer(Modifier.width(8.dp))
                // Hora — texto pequeño de 1 línea
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
            }
            Spacer(Modifier.height(8.dp))
            // Descripción — placeholder compacto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )
            // Espaciador inferior para igualar la altura de la card real
            Spacer(Modifier.height(24.dp))
        }
    }
}

/* ------------------------------------------------ */
/* Empty state                                      */
/* ------------------------------------------------ */
@Composable
private fun NotificationEmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.NotificationsNone,
                contentDescription = null,
                tint = VerviColors.TextGray,
                modifier = Modifier.size(64.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "No hay notificaciones",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = VerviColors.TextDark
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Las notificaciones nuevas aparecerán aquí",
                fontSize = 13.sp,
                color = VerviColors.TextGray
            )
        }
    }
}

/* ------------------------------------------------ */
/* Error state                                      */
/* ------------------------------------------------ */
@Composable
private fun NotificationErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = VerviColors.CancelRed,
                modifier = Modifier.size(48.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = message,
                fontSize = 14.sp,
                color = VerviColors.TextDark,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Spacer(Modifier.height(16.dp))
            VerviSmallButton(
                text = "Reintentar",
                color = VerviColors.Blue,
                onClick = onRetry,
                modifier = Modifier.widthIn(min = 180.dp)
            )
        }
    }
}
