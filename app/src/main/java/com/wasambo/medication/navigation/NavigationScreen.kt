package com.wasambo.medication.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.wasambo.medication.R

sealed class Screen(
    val route: String,
    @StringRes val title: Int = R.string.app_name,
    val navIcon: (@Composable () -> Unit) = {
        Icon(
            Icons.Filled.Home, contentDescription = "home"
        )
    },
    val objectName: String = "",
    val objectPath: String = ""
) {
    object Login : Screen("login")
    object Home : Screen("home")
    object DrugDetail : Screen(
        route = "drug_detail/{drugId}",
        objectName = "drugId",
        objectPath = "/{drugId}"
    ) {
        fun createRoute(drugId: String) = "drug_detail/$drugId"
    }
}