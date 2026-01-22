package com.raduleu.habithaven.feature.agenda

import com.raduleu.habithaven.core.model.FocusWithChildren

sealed interface AgendaUiState {
    data object Loading : AgendaUiState
    data object Empty : AgendaUiState
    data class Success(val focuses: List<FocusWithChildren>) : AgendaUiState
}