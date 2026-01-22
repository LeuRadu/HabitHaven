package com.raduleu.habithaven.feature.agenda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raduleu.habithaven.core.data.repository.FocusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AgendaViewModel @Inject constructor(
    focusRepository: FocusRepository,
) : ViewModel() {

    val uiState: StateFlow<AgendaUiState> = focusRepository.getActiveFocusesWithChildren()
        .map { focuses ->
            if (focuses.isEmpty()) {
                AgendaUiState.Empty
            } else {
                AgendaUiState.Success(focuses)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AgendaUiState.Loading
        )
}

