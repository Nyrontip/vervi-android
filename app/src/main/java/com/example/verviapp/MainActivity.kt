package com.example.verviapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import com.example.verviapp.ui.theme.VerviAppTheme
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.verviapp.ui.screens.SplashScreen
import com.example.verviapp.ui.screens.HomeScreen
import com.example.verviapp.ui.screens.LoginScreen
import com.example.verviapp.ui.screens.PrestadoresScreen
import com.example.verviapp.ui.screens.ProfileScreen
import com.example.verviapp.ui.screens.ApplyForServiceScreen
import com.example.verviapp.ui.screens.EditProfileScreen
import com.example.verviapp.ui.screens.NewRequestScreen
import com.example.verviapp.ui.screens.NotificationsScreen
import com.example.verviapp.ui.screens.RateServiceScreen
import com.example.verviapp.ui.screens.RequestCancelScreen
import com.example.verviapp.ui.screens.RequestConfirmScreen
import com.example.verviapp.ui.screens.RequestDetailsScreen
import com.example.verviapp.ui.screens.ServiceDetailsScreen
import com.example.verviapp.ui.screens.RequestsScreen
import com.example.verviapp.ui.screens.ServiceHistoryScreen
import com.example.verviapp.ui.screens.ChatScreen

@AndroidEntryPoint
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
                    dialog(
                        route = "service/rate/{serviceId}",
                        arguments = listOf(navArgument("serviceId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val serviceId = backStackEntry.arguments?.getInt("serviceId") ?: return@dialog
                        RateServiceScreen(navController, serviceId)
                    }
                    composable("chat") {
                        ChatScreen(navController)
                    }
                    composable("request/details") {
                        RequestDetailsScreen(navController)
                    }
                    composable("request/details/{requestId}") {
                        RequestDetailsScreen(navController)
                    }
                    dialog(
                        route = "request/cancel/{requestId}",
                        arguments = listOf(navArgument("requestId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val requestId = backStackEntry.arguments?.getInt("requestId") ?: return@dialog
                        RequestCancelScreen(navController, requestId)
                    }
                    composable("request/applications/{requestId}") {
                        // TODO: pantalla de postulaciones para el dueño de la solicitud
                    }
                    dialog(
                        route = "request/confirm/{requestId}",
                        arguments = listOf(navArgument("requestId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val requestId = backStackEntry.arguments?.getInt("requestId") ?: return@dialog
                        RequestConfirmScreen(navController, requestId)
                    }
                    composable(
                        route = "service/details/{serviceId}",
                        arguments = listOf(navArgument("serviceId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val serviceId = backStackEntry.arguments?.getInt("serviceId") ?: return@composable
                        ServiceDetailsScreen(navController, serviceId)
                    }
                    dialog(
                        route = "service/apply/{requestId}",
                        arguments = listOf(navArgument("requestId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val requestId = backStackEntry.arguments?.getInt("requestId") ?: return@dialog
                        ApplyForServiceScreen(navController, requestId)
                    }

                }
            }
        }
    }
}
