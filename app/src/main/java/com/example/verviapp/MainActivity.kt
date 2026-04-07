package com.example.verviapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.verviapp.Presentation.ui.theme.VerviAppTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.verviapp.Presentation.ui.screens.ApplyForServiceScreen
import com.example.verviapp.Presentation.ui.screens.EditProfileScreen
import com.example.verviapp.Presentation.ui.screens.NewRequestScreen
import com.example.verviapp.Presentation.ui.screens.NotificationsScreen
import com.example.verviapp.Presentation.ui.screens.RateServiceScreen
import com.example.verviapp.Presentation.ui.screens.RequestCancelScreen
import com.example.verviapp.Presentation.ui.screens.RequestConfirmScreen
import com.example.verviapp.Presentation.ui.screens.RequestDetailsScreen
import com.example.verviapp.Presentation.ui.screens.ServiceDetailsScreen
import com.example.verviapp.Presentation.ui.screens.RequestsScreen
import com.example.verviapp.Presentation.ui.screens.ServiceHistoryScreen
import com.example.verviapp.Presentation.ui.screens.ChatScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            _root_ide_package_.com.example.verviapp.Presentation.ui.theme.VerviAppTheme {
                NavHost(
                    navController = navController,
                    startDestination = "splash"
                ) {
                    composable("splash") {
                        _root_ide_package_.com.example.verviapp.SplashScreen(navController)
                    }
                    composable("login") {
                        _root_ide_package_.com.example.verviapp.LoginScreen(navController)
                    }
                    composable("home") {
                        _root_ide_package_.com.example.verviapp.HomeScreen(navController)
                    }
                    composable("prestadores") {
                        _root_ide_package_.com.example.verviapp.PrestadoresScreen(navController)
                    }
                    // Ruta con parámetro opcional — el ? hace que sea nullable
                    // Sin parámetro → perfil del usuario logueado
                    // Con parámetro → perfil del prestador con ese ID
                    composable("profile?userId={userId}") { backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("userId")
                        _root_ide_package_.com.example.verviapp.ProfileScreen(navController, userId)
                    }
                    composable("editProfile") {
                        _root_ide_package_.com.example.verviapp.Presentation.ui.screens.EditProfileScreen(
                            navController
                        )
                    }
                    composable("notifications") {
                        _root_ide_package_.com.example.verviapp.Presentation.ui.screens.NotificationsScreen(
                            navController
                        )
                    }
                    composable("requests/management") {
                        _root_ide_package_.com.example.verviapp.Presentation.ui.screens.RequestsScreen(
                            navController
                        )
                    }
                    composable("request/new") {
                        _root_ide_package_.com.example.verviapp.Presentation.ui.screens.NewRequestScreen(
                            navController
                        )
                    }
                    composable("services/history") {
                        _root_ide_package_.com.example.verviapp.Presentation.ui.screens.ServiceHistoryScreen(
                            navController
                        )
                    }
                    composable("service/rate") {
                        _root_ide_package_.com.example.verviapp.Presentation.ui.screens.RateServiceScreen(
                            navController
                        )
                    }
                    composable("chat") {
                        _root_ide_package_.com.example.verviapp.Presentation.ui.screens.ChatScreen(
                            navController
                        )
                    }
                    composable("request/details") {
                        _root_ide_package_.com.example.verviapp.Presentation.ui.screens.RequestDetailsScreen(
                            navController
                        )
                    }
                    composable("request/cancel") {
                        _root_ide_package_.com.example.verviapp.Presentation.ui.screens.RequestCancelScreen(
                            navController
                        )
                    }
                    composable("request/confirm") {
                        _root_ide_package_.com.example.verviapp.Presentation.ui.screens.RequestConfirmScreen(
                            navController
                        )
                    }
                    composable("service/details") {
                        _root_ide_package_.com.example.verviapp.Presentation.ui.screens.ServiceDetailsScreen(
                            navController
                        )
                    }
                    composable("service/apply") {
                        _root_ide_package_.com.example.verviapp.Presentation.ui.screens.ApplyForServiceScreen(
                            navController
                        )
                    }

                }
            }
        }
    }
}
