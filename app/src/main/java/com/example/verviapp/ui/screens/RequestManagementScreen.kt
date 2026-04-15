package com.example.verviapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.verviapp.ui.components.*
import com.example.verviapp.ui.theme.VerviColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

// -----------------------------
// Data Class
// -----------------------------
data class RequestItem(
    val id: Int,
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
// SCREEN
// -----------------------------
@Composable
fun RequestsScreen(navController: NavController) {

    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            VerviTopBar(
                title = "Solicitudes",
                onBack = { navController.popBackStack() }
            )
        },
        bottomBar = {
            VerviBottomBar(navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("request/new") },
                containerColor = VerviColors.FabOrange,
                contentColor = VerviColors.TextWhite
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Text(
                        "Nueva Solicitud",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(VerviColors.BgColor)
        ) {

            // 🔵 Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = VerviColors.BgColor,
                contentColor = VerviColors.Blue
            ) {
                listOf("Activas", "Finalizadas").forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 15.sp,
                                fontWeight = if (selectedTab == index)
                                    FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // 🔵 Lista de solicitudes
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sampleRequests.size) { index ->
                    VerviRequestCard(
                        navController = navController,
                        request = sampleRequests[index]
                    )
                }
            }
        }
    }
}

// -----------------------------
// SAMPLE DATA
// -----------------------------
val sampleRequests = listOf(
    RequestItem(
        id = 1,
        status = "En curso",
        statusColor = Color(0xFF10B981),
        title = "Mantenimiento de Aire Acondicionado",
        date = "12 Oct 2023",
        applications = "3 Postulaciones",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB67969hdR51hZJ4MMl2KcD7Be19oLLeVWL6ROGoIgDcM5U8XflIO3tkfgCzwuH2MJSl_8_Gheu3jkhhVNTufiHwaXoHb7SQgsbVTt4ldPwE8EZWXv_CVNcD3c_ohEvLSEQECdZzXvKyJzDDMy7F2KJt6ksuHGcG9YPaLmghhRMZ-w50KFxTEAAEexjyZI93LFChiCnrTJaogs1fQs6Zg7JZew6NBm_Ul9K9ZD0v3ViKD5bnk4NnJMmmYK7kKDu899cW_Owf4R2VWgC",
        buttonText = "Gestionar",
        secondaryIcon = Icons.Default.MoreHoriz
    ),
    RequestItem(
        id = 2,
        status = "Pendiente",
        statusColor = Color(0xFFF59E0B),
        title = "Clase Particular de Matemáticas",
        date = "10 Oct 2023",
        applications = "1 Postulación",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBnl5p6ez-eg1L9zbqiPi776Iuugs_VciEqTLCanHi4PTptvUeBTYJuf-37hVntOaYzL8bDnsTTKRHFJwN13XDMvun57PjsABpdDapeKr5jXLYXSeSCOgbXTSjBlR0fXGcccOJv4v65jC0UpTk08VTtb8zFh-dMNEbmLlrK_d1oKkSCctoal7ERBV0fzFgeWYoZgzuJkqdx3GOQcCisgg-a5Q62_eZGbY52d-xyOXEzl-9nVrhRf83hHkWdJb6apj-WOF2Jhibnkhgr",
        buttonText = "Gestionar",
        secondaryIcon = Icons.Default.MoreHoriz
    ),
    RequestItem(
        id = 3,
        status = "Borrador",
        statusColor = Color.Gray,
        title = "Reparación de Fuga de Agua",
        date = "Hoy",
        applications = "0 Postulaciones",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuA1bULF4g5fXFqhPjmuBYe3t3C2ROTKCc-X0AhOtpq620jekQb5iaUTK4G9oJP8fgGCm0gAyfMrtOmohM7ZaGZWqqQAnEhUOuusSHcDw25yFhxbiVhJx49fM57C8qxUHvQOcZ9IhDMoGNVrjiSWf7FMd7ypM-yxc9r2N8jGchiccQVbJNk3dabVUAOzIsaFOHeo9KzbjjMGsXseOWuLzOKdRN7EAguL-tsgVPTd-Dcs-joD9YSmKv6lHC8brYllkaAd8GFT31KpmHQv",
        buttonText = "Continuar",
        secondaryIcon = Icons.Default.DeleteOutline
    )
)