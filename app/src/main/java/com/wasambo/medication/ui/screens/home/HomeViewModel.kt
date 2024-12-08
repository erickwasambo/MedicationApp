package com.wasambo.medication.ui.screens.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasambo.medication.data.model.ProblemData
import com.wasambo.medication.data.model.Problems
import com.wasambo.medication.data.repository.local.medical.LocalMedicalRepository
import com.wasambo.medication.data.repository.remote.medical.MedicalRepository
import com.wasambo.medication.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application,
    private val medicalRepository: MedicalRepository,
    private val localMedicalRepository: LocalMedicalRepository
) : ViewModel() {
    private val _medicalData = MutableStateFlow<Map<String, List<ProblemData>>>(emptyMap())
    val medicalData = _medicalData.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _isConnected = MutableStateFlow(true)
    val isConnected = _isConnected.asStateFlow()

    fun getGreeting(): String {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        
        val greeting = when (hourOfDay) {
            in 0..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else -> "Good Night"
        }
        
        val sharedPrefs = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val username = sharedPrefs.getString("username", "User") ?: "User"
        
        return "$greeting, $username!"
    }

    fun fetchMedicalData() {
        viewModelScope.launch {
            // Assuming the repository returns data with problem-drug relationships
            try {
                medicalRepository.fetchAndSaveMedicalData()
                // After saving, get the data from local DB
                val data = localMedicalRepository.getProblems()
                _medicalData.value = data
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                _isRefreshing.value = true
                // Use existing method to fetch and save data
                medicalRepository.fetchAndSaveMedicalData()
                // Get updated data from local DB
                val refreshedData = localMedicalRepository.getProblems()
                _medicalData.value = refreshedData
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun setRefreshing(refreshing: Boolean) {
        _isRefreshing.value = refreshing
    }

    fun updateConnectivityStatus(isConnected: Boolean) {
        _isConnected.value = isConnected
    }
} 