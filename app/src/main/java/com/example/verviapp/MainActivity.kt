package com.example.verviapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.verviapp.ui.theme.VerviAppTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.verviapp.ui.screens.ApplyForServiceScreen
import com.example.verviapp.ui.screens.EditProfileScreen
import com.example.verviapp.ui.screens.NewRequestScreen
import com.example.verviapp.ui.screens.NotificationsScreen
import com.example.verviapp.ui.screens.RateServiceScreen
import com.example.verviapp.ui.screens.RequestDetailsScreen
import com.example.verviapp.ui.screens.ServiceDetailsScreen
import com.example.verviapp.ui.screens.RequestsScreen
import com.example.verviapp.ui.screens.ServiceHistoryScreen
import com.example.verviapp.ui.screens.ChatScreen

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
                    // Ruta con parámetro opcional — el ? hace que sea nullable
                    // Sin parámetro → perfil del usuario logueado
                    // Con parámetro → perfil del prestador con ese ID
                    composable("profile?userId={userId}") { backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("userId")
                        ProfileScreen(navController, userId)
                    }
                    composable("editProfile") {
                        EditProfileScreen(navController)
                    }
                    composable("notifications") {
                        NotificationsScreen(navController)
                    }
                    composable("requests/management") {
                        RequestsScreen(navController)
                    }
                    composable("request/new") {
                        NewRequestScreen(navController)
                    }
                    composable("services/history") {
                        ServiceHistoryScreen(navController)
                    }
                    composable("service/rate") {
                        RateServiceScreen(navController)
                    }
                    composable("chat") {
                        ChatScreen(navController)
                    }
                    composable("request/details") {
                        RequestDetailsScreen(navController)
                    }
                    composable("service/details") {
                        ServiceDetailsScreen(navController)
                    }
                    composable("service/apply") {
                        ApplyForServiceScreen(navController)
                    }

                }
            }
        }
    }
}
