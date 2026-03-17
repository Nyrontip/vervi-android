package com.example.verviapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.verviapp.ui.theme.VerviAppTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.verviapp.ui.screens.NotificationsScreen
import com.example.verviapp.ui.screens.RequestsScreen
import com.example.verviapp.ui.screens.ServiceHistoryScreen
import com.example.verviapp.ui.screens.RateServiceScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            VerviAppTheme {
                NavHost(
                    navController = navController,
                    startDestination = "splash"
                ) {
                    composable("splash") {
                        SplashScreen(navController)
                    }
                    composable("login") {
                        LoginScreen(navController)
                    }
                    composable("home") {
                        HomeScreen(navController)
                    }
                    composable("prestadores") {
                        PrestadoresScreen(navController)
                    }
                    composable("profile") {
                        ProfileScreen(navController)
                    }
                    composable("notifications") {
                        NotificationsScreen(navController)
                    }
                    composable("requests/management") {
                        RequestsScreen(navController)
                    }
                    composable("services/history") {
                        ServiceHistoryScreen(navController)
                    }
                    composable("service/rate") {
                        RateServiceScreen(navController)
                    }

                }
            }
        }
    }
}
