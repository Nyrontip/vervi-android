package com.example.verviapp.ui.screens

import android.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.verviapp.ui.components.VerviBottomBar
import com.example.verviapp.ui.theme.VerviColors

/// -----------------------------
// Data Classes
// -----------------------------
data class RequestItem(
    val status: String,
    val statusColor: Color,
    val title: String,
    val date: String,
    val applications: String,
    val imageUrl: String,
    val buttonText: String,
    val secondaryIcon: ImageVector
)

// -----------------------------
// ATOMS
// -----------------------------
@Composable
fun StatusDot(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(8.dp)
            .background(color, CircleShape)
    )
}

@Composable
fun TextSmall(text: String, color: Color = VerviColors.TextSecondary, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = 12.sp,
        color = color,
        modifier = modifier
    )
}

@Composable
fun TextMedium(
    text: String,
    color: Color = VerviColors.TextPrimary,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = color,
        fontWeight = fontWeight,
        modifier = modifier
    )
}

@Composable
fun TextLarge(
    text: String,
    color: Color = VerviColors.TextPrimary,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Bold
) {
    Text(
        text = text,
        fontSize = 18.sp,
        color = color,
        fontWeight = fontWeight,
        modifier = modifier
    )
}

@Composable
fun IconButtonAtom(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    background: Color = VerviColors.Transparent,
    iconTint: Color = VerviColors.TextPrimary,
    size: Dp = 40.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .background(background, RoundedCornerShape(8.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = iconTint)
    }
}

// -----------------------------
// MOLECULES
// -----------------------------
@Composable
fun RequestCard(request: RequestItem, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(VerviColors.CardBackground, RoundedCornerShape(12.dp))
            .border(1.dp, VerviColors.BorderGray, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    StatusDot(color = request.statusColor)
                    TextSmall(text = request.status.uppercase())
                }
                TextMedium(text = request.title, fontWeight = FontWeight.Bold)
                TextSmall(
                    text = "${request.date} • ${request.applications}",
                    color = VerviColors.Primary
                )
            }

            Row(modifier = Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = VerviColors.Primary.copy(alpha = 0.1f)),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Text(
                        request.buttonText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerviColors.Primary
                    )
                }

                IconButtonAtom(icon = request.secondaryIcon, onClick = {})
            }
        }

        AsyncImage(
            model = request.imageUrl,
            contentDescription = request.title,
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

// -----------------------------
// ORGANISMS
// -----------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderBar(navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            Text("Solicitudes", color = VerviColors.TextPrimary)
        },

        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(Icons.Default.ArrowBack, null, tint = VerviColors.TextPrimary)
            }
        }
    )
}

// -----------------------------
// SCREEN / TEMPLATE
// -----------------------------
@Composable
fun RequestsScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    Scaffold(
        topBar = { HeaderBar(navController) },
        bottomBar = {
            VerviBottomBar(navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = VerviColors.FabOrange,
                contentColor = VerviColors.TextWhite
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Text("Nueva Solicitud", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { padding ->

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor   = VerviColors.BgColor,
            contentColor     = VerviColors.Blue
        ) {
            listOf("Activas", "Finalizadas").forEachIndexed { index, title ->
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

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(VerviColors.BgColor)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleRequests.size) { index ->
                RequestCard(request = sampleRequests[index])
            }
        }
    }
}

val sampleRequests = listOf(
    RequestItem(
        status = "En curso",
        statusColor = Color(0xFF10B981),
        title = "Mantenimiento de Aire Acondicionado",
        date = "12 Oct 2023",
        applications = "3 Postulaciones",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB67969hdR51hZJ4MMl2KcD7Be19oLLeVWL6ROGoIgDcM5U8XflIO3tkfgCzwuH2MJSl_8_Gheu3jkhhVNTufiHwaXoHb7SQgsbVTt4ldPwE8EZWXv_CVNcD3c_ohEvLSEQECdZzXvKyJzDDMy7F2KJt6ksuHGcG9YPaLmghhRMZ-w50KFxTEAAEexjyZI93LFChiCnrTJaogs1fQs6Zg7JZew6NBm_Ul9K9ZD0v3ViKD5bnk4NnJMmmYK7kKDu899cW_Owf4R2VWgC",
        buttonText = "Gestionar",
        secondaryIcon = Icons.Filled.MoreHoriz
    ),
    RequestItem(
        status = "Pendiente",
        statusColor = Color(0xFFF59E0B),
        title = "Clase Particular de Matemáticas",
        date = "10 Oct 2023",
        applications = "1 Postulación",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBnl5p6ez-eg1L9zbqiPi776Iuugs_VciEqTLCanHi4PTptvUeBTYJuf-37hVntOaYzL8bDnsTTKRHFJwN13XDMvun57PjsABpdDapeKr5jXLYXSeSCOgbXTSjBlR0fXGcccOJv4v65jC0UpTk08VTtb8zFh-dMNEbmLlrK_d1oKkSCctoal7ERBV0fzFgeWYoZgzuJkqdx3GOQcCisgg-a5Q62_eZGbY52d-xyOXEzl-9nVrhRf83hHkWdJb6apj-WOF2Jhibnkhgr",
        buttonText = "Gestionar",
        secondaryIcon = Icons.Filled.MoreHoriz
    ),
    RequestItem(
        status = "Borrador",
        statusColor = Color.Gray,
        title = "Reparación de Fuga de Agua",
        date = "Hoy",
        applications = "0 Postulaciones",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuA1bULF4g5fXFqhPjmuBYe3t3C2ROTKCc-X0AhOtpq620jekQb5iaUTK4G9oJP8fgGCm0gAyfMrtOmohM7ZaGZWqqQAnEhUOuusSHcDw25yFhxbiVhJx49fM57C8qxUHvQOcZ9IhDMoGNVrjiSWf7FMd7ypM-yxc9r2N8jGchiccQVbJNk3dabVUAOzIsaFOHeo9KzbjjMGsXseOWuLzOKdRN7EAguL-tsgVPTd-Dcs-joD9YSmKv6lHC8brYllkaAd8GFT31KpmHQv",
        buttonText = "Continuar",
        secondaryIcon = Icons.Filled.DeleteOutline
    )
)
