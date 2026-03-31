package com.example.verviapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
// ...keeping LazyColumn, using item {} inside loop to avoid overload ambiguity
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.verviapp.ui.components.*
import com.example.verviapp.ui.theme.VerviColors
import com.example.verviapp.model.ServiceHistoryItem
import com.example.verviapp.viewmodel.ServiceHistoryUiState
import com.example.verviapp.repository.ServiceHistoryRepositoryImpl
import com.example.verviapp.viewmodel.ServiceHistoryViewModel
import com.example.verviapp.viewmodel.ServiceHistoryViewModelFactory


/* ------------------------------------------------ */
/* SCREEN */
/* ------------------------------------------------ */

@Composable
fun ServiceHistoryScreen(navController: NavController, vm: ServiceHistoryViewModel = viewModel<ServiceHistoryViewModel>(factory = ServiceHistoryViewModelFactory(ServiceHistoryRepositoryImpl()))) {

    val state by vm.uiState.collectAsState(initial = ServiceHistoryUiState())

    // Manejo de eventos de navegación emitidos desde el ViewModel
    LaunchedEffect(Unit) {
        vm.navigationEvents.collect { event ->
            when (event) {
                is com.example.verviapp.viewmodel.ServiceHistoryEvent.OpenDetails -> {
                    navController.navigate("service/details")
                }
                is com.example.verviapp.viewmodel.ServiceHistoryEvent.OpenRate -> {
                    navController.navigate("service/rate")
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.systemBarsPadding(),

        topBar = {
            VerviTopBar(
                title = "Historial de servicios",
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

            // 🔵 Tabs reutilizables
            VerviTabs(
                tabs = listOf("Como cliente", "Como prestador"),
                selectedIndex = state.selectedTab,
                onTabSelected = { vm.selectTab(it) }
            )

            // 🔵 Lista
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // Evitar ambigüedad con overloads de items(...)
                for (service in state.services) {
                    item {
                        VerviServiceHistoryCard(
                            item = service,
                            onClick = { vm.onServiceClick(service) },
                            onRate = { vm.onRate(service) }
                        )
                    }
                }
            }
        }
    }
}

// sample data moved to ServiceHistoryRepositoryImpl
