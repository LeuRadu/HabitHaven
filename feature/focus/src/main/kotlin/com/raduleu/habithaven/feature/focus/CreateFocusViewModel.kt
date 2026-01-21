package com.raduleu.habithaven.feature.focus

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raduleu.habithaven.core.data.repository.FocusRepository
import com.raduleu.habithaven.core.model.Focus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateFocusViewModel @Inject constructor(
    private val focusRepository: FocusRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateFocusUiState())
    val uiState: StateFlow<CreateFocusUiState> = _uiState.asStateFlow()

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun updateIconName(iconName: String) {
        _uiState.update { it.copy(iconName = iconName) }
    }

    fun updateColorIndex(colorIndex: Int) {
        _uiState.update { it.copy(colorIndex = colorIndex) }
    }

    fun saveFocus() {
        val currentState = _uiState.value
        if (currentState.name.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val newFocus = Focus(
                    id = UUID.randomUUID().toString(),
                    name = currentState.name,
                    iconName = currentState.iconName,
                    colorIndex = currentState.colorIndex,
                    sortOrder = 0,
                    isArchived = false,
                )
                focusRepository.upsertFocus(newFocus)
                _uiState.update { it.copy(isSaved = true) }
            } catch (e: Exception) {
                Log.e("CreateFocusViewModel", "Error adding new focus. Exception: ${e.message}")
                // TODO: Add userMessage to state to show a Snackbar
            } finally {
                // This block runs whether it succeeded or failed
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onFocusSaved() {
        _uiState.update { it.copy(isSaved = false) }
    }
}
