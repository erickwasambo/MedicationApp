package com.wasambo.medication.ui.screens.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.wasambo.medication.navigation.Navigation
import com.wasambo.medication.ui.screens.home.HomeViewModel
import com.wasambo.medication.ui.screens.login.LoginViewModel
import com.wasambo.medication.utils.ConnectivityObserver
import com.wasambo.medication.utils.NetworkConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val splashViewModel: MainActivityViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var connectivityObserver: ConnectivityObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition { splashViewModel.isLoading.value }
        }
        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        setContent {
            val navController = rememberNavController()
            
            LaunchedEffect(Unit) {
                connectivityObserver.observe().collect { status ->
                    val isConnected = status == ConnectivityObserver.Status.Available
                    homeViewModel.updateConnectivityStatus(isConnected)
                    loginViewModel.updateConnectivityStatus(isConnected)
                }
            }
            
            Navigation(navController = navController)
        }
    }
}