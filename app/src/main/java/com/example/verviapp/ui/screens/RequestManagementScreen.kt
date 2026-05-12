package com.example.verviapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.verviapp.ui.components.*
import com.example.verviapp.ui.theme.VerviColors
import com.example.verviapp.viewmodel.RequestManagementViewModel

// -----------------------------
// SCREEN
// -----------------------------
@Composable
fun RequestsScreen(navController: NavController, vm: RequestManagementViewModel = hiltViewModel()) {

    val state by vm.uiState.collectAsState()

    // Navegación por eventos
    LaunchedEffect(Unit) {
        vm.events.collect { ev ->
            when (ev) {
                is com.example.verviapp.viewmodel.RequestEvent.CreateNew -> {
                    navController.navigate("request/new")
                }
                else -> {}
            }
        }
    }

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
                onClick = { vm.onCreateNew() },
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

            // Tabs
            TabRow(
                selectedTabIndex = state.selectedTab,
                containerColor = VerviColors.BgColor,
                contentColor = VerviColors.Blue
            ) {
                listOf("Activas", "Finalizadas").forEachIndexed { index, title ->
                    Tab(
                        selected = state.selectedTab == index,
                        onClick = { vm.selectTab(index) },
                        text = {
                            Text(
                                text = title,
                                fontSize = 15.sp,
                                fontWeight = if (state.selectedTab == index)
                                    FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            when {
                // Cargando
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = VerviColors.Blue)
                    }
                }

                // Error
                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
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
                                text = state.error!!,
                                fontSize = 14.sp,
                                color = VerviColors.TextDark,
                                modifier = Modifier.padding(horizontal = 32.dp),
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(16.dp))
                            VerviSmallButton(
                                text = "Reintentar",
                                color = VerviColors.Blue,
                                onClick = { vm.retry() },
                                modifier = Modifier.widthIn(min = 180.dp)
                            )
                        }
                    }
                }

                // Vacío
                state.requests.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.ListAlt,
                                contentDescription = null,
                                tint = VerviColors.TextGray,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = if (state.selectedTab == 0)
                                    "No tienes solicitudes activas"
                                else
                                    "No tienes solicitudes finalizadas",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = VerviColors.TextDark
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "Crea una nueva solicitud para empezar",
                                fontSize = 13.sp,
                                color = VerviColors.TextGray
                            )
                        }
                    }
                }

                // Datos
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.requests.size) { index ->
                            val request = state.requests[index]
                            VerviRequestCard(
                                navController = navController,
                                request = request,
                                onClick = { navController.navigate("request/details/${request.id}") }
                            )
                        }
                    }
                }
            }
        }
    }
}
