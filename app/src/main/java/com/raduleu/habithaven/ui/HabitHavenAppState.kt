package com.raduleu.habithaven.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberHabitHavenAppState(
    navController: NavHostController = rememberNavController(),
): HabitHavenAppState {
    return remember(navController) {
        HabitHavenAppState(navController)
    }
}

@Stable
class HabitHavenAppState(
    val navController: NavHostController,
) {
    fun popBackStack() {
        navController.popBackStack()
    }
}