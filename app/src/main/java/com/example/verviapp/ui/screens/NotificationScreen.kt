package com.example.verviapp.ui.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.example.verviapp.ui.theme.VerviColors
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.verviapp.viewmodel.NotificationsViewModel
import com.example.verviapp.viewmodel.state.NotificationType


/* ------------------------------------------------ */
/* SAMPLE DATA */
/* ------------------------------------------------ */
// Nota: los datos de ejemplo ahora los provee el repositorio a través del ViewModel.

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
    val notifications = uiState.notifications

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

            // Lista de notificaciones usando componente local
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            ) {
                items(notifications) { item ->
                    VerviNotificationCard(
                        title = item.title,
                        description = item.description,
                        time = item.time,
                        type = item.type,
                        unread = item.unread,
                        onClick = {
                            // Marcar como leído al abrir
                            if (item.unread) viewModel.markAsRead(item)
                            // Acción según tipo de notificación: por ahora navegamos a detalles genéricos
                        }
                    )
                }
            }
        }
    }
}
