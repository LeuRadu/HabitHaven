package com.raduleu.habithaven.feature.focus

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raduleu.habithaven.core.data.repository.FocusRepository
import com.raduleu.habithaven.core.model.Focus
import com.raduleu.habithaven.feature.focus.navigation.FOCUS_ID_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class FocusFormViewModel @Inject constructor(
    private val focusRepository: FocusRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val focusId: String? = savedStateHandle[FOCUS_ID_ARG]

    private val _uiState = MutableStateFlow(FocusFormUiState())
    val uiState: StateFlow<FocusFormUiState> = _uiState.asStateFlow()

    init {
        if (focusId != null) {
            loadFocus(focusId)
        }
    }

    private fun loadFocus(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val focus = focusRepository.getFocusById(id)

            if (focus != null) {
                _uiState.update {
                    it.copy(
                        name = focus.name,
                        iconName = focus.iconName,
                        colorIndex = focus.colorIndex,
                        isLoading = false,
                        isEditing = true,
                        sortOrder = focus.sortOrder
                    )
                }
            } else {
                // Handle edge case: ID passed but not found in DB
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

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
                val idToSave = focusId ?: UUID.randomUUID().toString()
                val focusToSave = Focus(
                    id = idToSave,
                    name = currentState.name,
                    iconName = currentState.iconName,
                    colorIndex = currentState.colorIndex,
                    sortOrder = currentState.sortOrder,
                    isArchived = false,
                )
                focusRepository.upsertFocus(focusToSave)
                _uiState.update { it.copy(isSaved = true) }
            } catch (e: Exception) {
                Log.e("CreateFocusViewModel", "Error adding new focus. Exception: ${e.message}")
                // TODO: add userMessage to state to show a Snackbar
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onFocusSaved() {
        _uiState.update { it.copy(isSaved = false) }
    }

    fun onDeleteClick() {
        _uiState.update { it.copy(isDeleteDialogVisible = true) }
    }

    fun onDeleteDismiss() {
        _uiState.update { it.copy(isDeleteDialogVisible = false) }
    }

    fun confirmDelete() {
        val id = focusId ?: return // Should only happen in Edit mode anyway

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isDeleteDialogVisible = false) }

            val currentFocus = focusRepository.getFocusById(id)

            if (currentFocus != null) {
                focusRepository.archiveFocus(currentFocus.id)
                //TODO: should rename this something more generic
                _uiState.update { it.copy(isSaved = true) }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
