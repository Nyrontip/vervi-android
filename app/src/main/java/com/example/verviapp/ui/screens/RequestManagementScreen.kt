package com.example.verviapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.verviapp.ui.components.*
import com.example.verviapp.ui.theme.VerviColors
import com.example.verviapp.viewmodel.RequestManagementViewModel
import com.example.verviapp.viewmodel.RequestUiState

@Composable
fun RequestsScreen(navController: NavController, vm: RequestManagementViewModel = hiltViewModel()) {

    val state by vm.uiState.collectAsState()

    // Navegación por eventos
    LaunchedEffect(Unit) {
        vm.events.collect { ev ->
            when (ev) {
                is com.example.verviapp.viewmodel.RequestEvent.OpenRequest -> {
                    navController.navigate("request/details")
                }
                com.example.verviapp.viewmodel.RequestEvent.CreateNew -> {
                    navController.navigate("request/new")
                }
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

            // 🔵 Tabs
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

            // 🔵 Lista de solicitudes
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.requests.size) { index ->
                    VerviRequestCard(
                        navController = navController,
                        request = state.requests[index]
                    )
                }
            }
        }
    }
}

// -----------------------------
// SAMPLE DATA
// -----------------------------
// sample data moved to RequestRepositoryImpl
