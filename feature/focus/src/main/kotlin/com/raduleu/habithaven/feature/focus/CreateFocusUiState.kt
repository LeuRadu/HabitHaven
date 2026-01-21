package com.raduleu.habithaven.feature.focus

data class CreateFocusUiState(
    val name: String = "",
    val iconName: String = "category",
    val colorIndex: Int = 0,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)
