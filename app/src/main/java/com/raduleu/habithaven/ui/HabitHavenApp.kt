package com.raduleu.habithaven.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.raduleu.habithaven.navigation.HabitHavenNavHost

@Composable
fun HabitHavenApp(
    appState: HabitHavenAppState = rememberHabitHavenAppState()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        // bottomBar = { /* TODO: add currency BottomBar later? */ }
    ) { padding ->
        HabitHavenNavHost(
            appState = appState,
            modifier = Modifier.padding(padding)
        )
    }
}