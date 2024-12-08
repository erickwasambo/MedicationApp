package com.wasambo.medication.ui.screens.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasambo.medication.data.local.entity.DrugEntity
import com.wasambo.medication.data.repository.local.medical.LocalMedicalRepository
import com.wasambo.medication.data.repository.remote.medical.MedicalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrugDetailViewModel @Inject constructor(
    private val repo: MedicalRepository,
    private val localRepo: LocalMedicalRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val drugId: String = checkNotNull(savedStateHandle["drugId"])
    
    // Create a sealed class to represent UI state
    sealed class UiState {
        object Loading : UiState()
        data class Success(val drug: DrugEntity) : UiState()
        data class Error(val message: String) : UiState()
    }
    
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadDrug()
    }
    
    private fun loadDrug() {
        viewModelScope.launch {
            try {
                localRepo.findDrugByName(drugId)
                    .catch { exception ->
                        _uiState.value = UiState.Error(exception.message ?: "Unknown error occurred")
                    }
                    .collect { drugEntity ->
                        drugEntity?.let {
                            _uiState.value = UiState.Success(it)
                        } ?: run {
                            _uiState.value = UiState.Error("Drug not found")
                        }
                    }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}