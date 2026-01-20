package com.raduleu.habithaven.core.model

data class HabitCompletion(
    val id: String,
    val habitId: String,
    val chainId: String,
    val timestamp: Long,
    val value: Int
)