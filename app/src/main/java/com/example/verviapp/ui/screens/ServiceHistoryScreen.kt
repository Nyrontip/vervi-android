package com.example.verviapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.verviapp.ui.components.*
import com.example.verviapp.ui.theme.VerviColors
import com.example.verviapp.viewmodel.ServiceHistoryEvent
import com.example.verviapp.viewmodel.ServiceHistoryViewModel

@Composable
fun ServiceHistoryScreen(navController: NavController, vm: ServiceHistoryViewModel = hiltViewModel()) {

    val state by vm.uiState.collectAsState()

    LaunchedEffect(Unit) {
        vm.navigationEvents.collect { event ->
            when (event) {
                is ServiceHistoryEvent.OpenDetails -> {
                    navController.navigate("service/details/${event.item.serviceId}")
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            VerviTopBar(
                title = "Historial",
                onBack = { navController.popBackStack() }
            )
        },
        bottomBar = {
            VerviBottomBar(navController)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(VerviColors.BgColor)
        ) {

            VerviTabs(
                tabs = listOf("Activas", "Finalizadas"),
                selectedIndex = state.selectedTab,
                onTabSelected = { vm.selectTab(it) }
            )

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = VerviColors.Blue)
                    }
                }

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

                state.services.isEmpty() -> {
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
                                    "No tienes servicios activos"
                                else
                                    "No tienes servicios finalizados",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = VerviColors.TextDark
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "Cuando tus aplicaciones sean aceptadas",
                                fontSize = 13.sp,
                                color = VerviColors.TextGray
                            )
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = state.services,
                            key = { it.serviceId }
                        ) { service ->
                            VerviServiceHistoryCard(
                                item = service,
                                onClick = { vm.onServiceClick(service) }
                            )
                        }
                    }
                }
            }
        }
    }
}
