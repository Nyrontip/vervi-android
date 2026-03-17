package com.example.verviapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.verviapp.ui.components.VerviTopBar
import com.example.verviapp.ui.theme.VerviColors

// ---------- DATA ----------

// ---------- DATA ----------

data class NotificationItem(
    val title: String,
    val description: String,
    val time: String,
    val type: NotificationType,
    val unread: Boolean = false
)

enum class NotificationType {
    APPLICATION,
    MESSAGE,
    CONFIRMED,
    PAYMENT,
    REMINDER
}

// ---------- SAMPLE DATA ----------

val sampleNotifications = listOf(
    NotificationItem(
        "Nueva postulación recibida",
        "En el servicio: Reparación de tubería en Bogotá.",
        "hace 5 min",
        NotificationType.APPLICATION,
        true
    ),
    NotificationItem(
        "Nuevo mensaje de Juan",
        "¿A qué hora podrías venir a revisar el daño mañana?",
        "hace 15 min",
        NotificationType.MESSAGE
    ),
    NotificationItem(
        "Servicio confirmado",
        "Mantenimiento aire acondicionado ha sido agendado exitosamente.",
        "hace 1 h",
        NotificationType.CONFIRMED
    ),
    NotificationItem(
        "Pago recibido",
        "Has recibido COP $45.000 por Limpieza General.",
        "hace 3 h",
        NotificationType.PAYMENT
    ),
    NotificationItem(
        "Recordatorio de servicio",
        "Recuerda tu cita de mañana a las 8:00 AM para Jardinería.",
        "hace 5 h",
        NotificationType.REMINDER
    )
)

// ---------- ATOMS ----------

@Composable
fun NotificationIcon(type: NotificationType, unread: Boolean) {

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

    Box(
        modifier = Modifier.size(48.dp),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(background),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = tint)
        }

        if (unread) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 2.dp, y = (-2).dp)
                    .size(10.dp)
                    .background(VerviColors.OrangeSecondary, CircleShape)
                    .border(2.dp, VerviColors.TextWhite, CircleShape)
            )
        }
    }
}

// ---------- MOLECULES ----------

@Composable
fun NotificationRow(item: NotificationItem) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.Top
    ) {

        NotificationIcon(item.type, item.unread)

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Text(
                    item.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = VerviColors.TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    item.time,
                    fontSize = 11.sp,
                    color = VerviColors.TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                item.description,
                fontSize = 12.sp,
                color = VerviColors.TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun TabButton(
    title: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit,
) {

    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(12.5.dp))
        Text(
            title,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) VerviColors.Primary else VerviColors.TextSecondary
        )
        Spacer(modifier = Modifier.height(12.5.dp))
        Box(
            modifier = Modifier
                .height(2.5.dp)
                .fillMaxWidth()
                .background(
                    if (selected) VerviColors.Primary else VerviColors.Transparent
                )
        )
    }
}

// ---------- ORGANISMS ----------

@Composable
fun NotificationsList(notifications: List<NotificationItem>) {

    LazyColumn {

        items(notifications) { item ->

            NotificationRow(item)

            Divider(
                color = VerviColors.BorderGray,
                thickness = 0.5.dp
            )
        }
    }
}

// ---------- SCREEN ----------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }

    val notifications =
        if (selectedTab == 0)
            sampleNotifications
        else
            sampleNotifications.filter { it.unread }

    Scaffold(

        modifier = Modifier.systemBarsPadding(),

        topBar = {
            VerviTopBar("Notificaciones", { navController.popBackStack()})
        },

        bottomBar = {}

    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(VerviColors.BgColor)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(
                        0.5.dp,
                        VerviColors.BorderGray
                    )
                    .background(VerviColors.BackgroundLight)
            ) {
                TabButton(
                    "Todas",
                    selectedTab == 0,
                    modifier = Modifier.weight(1f)
                ) { selectedTab = 0 }

                TabButton(
                    "No leídas",
                    selectedTab == 1,
                    modifier = Modifier.weight(1f)
                ) { selectedTab = 1 }
            }

            NotificationsList(notifications)
        }
    }
}