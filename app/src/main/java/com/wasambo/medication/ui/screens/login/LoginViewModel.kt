package com.wasambo.medication.ui.screens.login

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasambo.medication.utils.ConnectivityObserver
import com.wasambo.medication.utils.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val application: Application,
    private val connectivityObserver: NetworkConnectivityObserver
) : ViewModel() {
    private val _email = mutableStateOf("")
    val email: String get() = _email.value
    
    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: Boolean get() = _isLoggedIn.value
    
    private val _loginError = MutableStateFlow<String?>(null)
    val loginError = _loginError.asStateFlow()
    
    private val _isConnected = MutableStateFlow(true)
    val isConnected = _isConnected.asStateFlow()
    
    init {
        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                _isConnected.value = status == ConnectivityObserver.Status.Available
            }
        }
    }
    
    private fun saveUsername(username: String) {
        val sharedPrefs = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().putString("username", username).apply()
    }
    
    fun login(username: String, password: String) {
        if (!_isConnected.value) {
            _loginError.value = "No internet connection. Please check your connection and try again."
            return
        }
        
        _email.value = username
        _isLoggedIn.value = true
        saveUsername(username)
        viewModelScope.launch {
            try {
                _loginError.value = null
            } catch (e: Exception) {
                _loginError.value = "Login failed: ${e.message}"
            }
        }
    }
    
    fun logout() {
        _email.value = ""
        _isLoggedIn.value = false
    }
    
    fun updateConnectivityStatus(isConnected: Boolean) {
        _isConnected.value = isConnected
    }
} 