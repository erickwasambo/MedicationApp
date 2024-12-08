package com.wasambo.medication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.wasambo.medication.data.model.Drug
import com.wasambo.medication.ui.screens.home.DrugDetailScreen
import com.wasambo.medication.ui.screens.home.HomeScreen
import com.wasambo.medication.ui.screens.login.LoginScreen

@Composable
fun Navigation(
    navController: NavHostController, drug: Drug? = null,
) {
    NavHost(navController, startDestination =  Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(
            route = Screen.DrugDetail.route,
            arguments = listOf(
                navArgument("drugId") {
                    type = NavType.StringType
                }
            )
        ) {
            DrugDetailScreen(navController = navController)
        }
    }
}


@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route?.substringBeforeLast("/")
}
