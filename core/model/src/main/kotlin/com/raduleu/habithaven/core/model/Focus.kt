package com.raduleu.habithaven.core.model

data class Focus(
    val id: String,
    val name: String,
    val iconName: String,
    val colorIndex: Int,
    val sortOrder: Int,
    val isArchived: Boolean,
)