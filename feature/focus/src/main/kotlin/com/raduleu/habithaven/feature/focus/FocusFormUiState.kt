package com.raduleu.habithaven.feature.focus

data class FocusFormUiState(
    val name: String = "",
    val iconName: String = "category",
    val colorIndex: Int = 0,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val isEditing: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,
    val sortOrder: Int = 0
)
