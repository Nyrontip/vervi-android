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
import com.example.verviapp.viewmodel.RequestEvent
import com.example.verviapp.viewmodel.RequestManagementViewModel
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

// -----------------------------
// SCREEN
// -----------------------------
@Composable
fun RequestsScreen(navController: NavController, vm: RequestManagementViewModel = hiltViewModel()) {

    val state by vm.uiState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                vm.retry()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Navegación por eventos
    LaunchedEffect(Unit) {
        vm.events.collect { ev ->
            when (ev) {
                is RequestEvent.CreateNew -> {
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
                listOf("Activas", "Finalizadas", "Borradores").forEachIndexed { index, title ->
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
                            val needsLogin = state.error?.contains("iniciar sesión") == true
                            VerviSmallButton(
                                text = if (needsLogin) "Iniciar sesión" else "Reintentar",
                                color = VerviColors.Blue,
                                onClick = {
                                    if (needsLogin) navController.navigate("login")
                                    else vm.retry()
                                },
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
                                text = when (state.selectedTab) {
                                    0 -> "No tienes solicitudes activas"
                                    1 -> "No tienes solicitudes finalizadas"
                                    else -> "No tienes borradores guardados"
                                },
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = VerviColors.TextDark
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = if (state.selectedTab == 2)
                                    "Los borradores aparecerán aquí al salir sin publicar"
                                else
                                    "Crea una nueva solicitud para empezar",
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
