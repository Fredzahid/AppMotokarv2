package com.fred.motocontrolapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.fred.motocontrolapp.ui.screen.*
import com.fred.motocontrolapp.ui.viewmodel.MainViewModel

@Composable
fun AppNavigation(viewModel: MainViewModel) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {
            HomeScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable("add_carrera") {
            AddCarreraScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable("add_gasto") {
            AddGastoScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable("historial") {
            HistorialScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}