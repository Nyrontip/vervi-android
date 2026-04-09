package com.example.verviapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.verviapp.ui.components.*
import com.example.verviapp.ui.theme.VerviColors
import com.example.verviapp.model.ServiceHistoryItem


/* ------------------------------------------------ */
/* SCREEN */
/* ------------------------------------------------ */

@Composable
fun ServiceHistoryScreen(navController: NavController) {

    var selectedTab by remember { mutableStateOf(0) }

    // 🔵 Datos simulados (puedes luego separarlos por tipo)
    val services = remember { sampleServices }

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
                selectedIndex = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            // 🔵 Lista
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(services) { service ->

                    VerviServiceHistoryCard(
                        item = service,

                        onClick = {
                            navController.navigate("service/details")
                        },

                        onRate = {
                            navController.navigate("service/rate")
                        }
                    )
                }
            }
        }
    }
}

/* ------------------------------------------------ */
/* SAMPLE DATA */
/* ------------------------------------------------ */

val sampleServices = listOf(

    ServiceHistoryItem(
        title = "Lavado de alfombras",
        provider = "Sofía García",
        date = "12 Oct 2023",
        price = "$85.000 COP",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCI7wY-NP3RRke6e9YPkC_C4JJGTY6-sZWwol_N6RmV9LxlHUdBmZT5tARZWZAz5kUFUK4KELUUjt4HEZBR_E7Fd0R5-C8cqi6u7BqfOgtytm03utzPZrD5p43rlNIcrJkBIpIOEsEubkaMSL5iKzWmKNAmlJKSAzM7mjQ-UTfwlVyD5trH-6VmfSv-TnCSMuXEmRhEOGo9OdKO90sLqbUJZL8yZQ4oaelTvg-B0Xn52YD8OEK1AaU9aQVvt5bm_z3_WWIWmip233u0",
        rating = 5f
    ),

    ServiceHistoryItem(
        title = "Reparación Eléctrica",
        provider = "Carlos Martínez",
        date = "05 Oct 2023",
        price = "$120.000 COP",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBij0dg11e1wjROPz0TeFPDue7tFDavmjgwzxrjSKLaYlyTML5uMU6LU9AdlGuxjUizWHMyR7qjkaQd9b6c86erHlBMKukYGUSLLJcbeOyTNrETPJj4lwtC7EspVj7cpPssQpnmJt4TlJzJ2hC40xH14_rtyPt2Q-UcNiXGIhoTEAjpAmTQxWEpomoEa6WnBNK7Y_oZRjjBWoIbeRc2wYkc7-7fP3EYpu-Iupe7jLgdEAg3-C53MFe6o0hzTDdLanaU7KPrVo3U_IXf",
        rating = 4f
    ),

    ServiceHistoryItem(
        title = "Paseo de Mascotas",
        provider = "Laura Beltrán",
        date = "28 Sep 2023",
        price = "$35.000 COP",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAVbi0mLTyrm5jOwg7uitaxLafYmlwSpi9VdKzhzU0oMxWAQ10hmG2Dhk-8S8b1QVJuBH9RXVEfLacsEzygQikVb8rLfOoSkehboWW-TMl1wI4f33YsFBQkCZquQRywjlLQBu0kR-1UNwQi81I0jt3GIkjRXT0QQCWZ6y2BlEghA82vRIbT4SzL3GHd6sFac1STgkGtdjiX_gbPG1TZlOgGfaAfTT9-_mUKkNWO7MPn4x3sUzrxK8WoLEwcvD8bAdrMcMevOLwbiXAM",
        rating = null
    ),

    ServiceHistoryItem(
        title = "Limpieza de Vidrios",
        provider = "Mateo Ruiz",
        date = "15 Sep 2023",
        price = "$60.000 COP",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBPErMB0nkS4BOI8vq_hidq8Uo7CW-9L5MwPLI_zEemUFIEtVLwQIC50llT75CPCoiBFFRhzvZwapXXUYfJ5sA_lOg94TfugYiqB5xFfD3cJ-C9RsjN-3x7UvhDIrDC1lc_p79SmXneXqMBkbn-bfUdSZewVaJZK9AdIkn5xCd3GU1da8S7CQw3OIltmEmIco5WktCWlPG7cEX8-Hp1bFE2b9gN0lqUDBRQitaQEAw6wHZtun4ZDs4kMw-A9LAT3XV0bQQPhKOab4zH",
        rating = 4.8f
    )
)