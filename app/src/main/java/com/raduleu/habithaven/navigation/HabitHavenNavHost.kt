package com.raduleu.habithaven.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.raduleu.habithaven.feature.agenda.agendaScreen
import com.raduleu.habithaven.feature.agenda.navigateToAgenda
import com.raduleu.habithaven.feature.focus.createFocusScreen
import com.raduleu.habithaven.feature.focus.navigateToCreateFocus
import com.raduleu.habithaven.ui.HabitHavenAppState

@Composable
fun HabitHavenNavHost(
    modifier: Modifier = Modifier,
    appState: HabitHavenAppState,
    startDestination: String = "home_route",
) {
    NavHost(
        modifier = modifier,
        navController = appState.navController,
        startDestination = startDestination,
    ) {
        // temp home screen
        composable("home_route") {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Button(onClick = {
                    appState.navController.navigateToAgenda()
                }) {
                    Text("Preview the app (work in progress)")
                }
            }
        }

        createFocusScreen(
            onBackClick = { appState.popBackStack() }
        )

        agendaScreen(
            onAddFocusButtonClick = { appState.navController.navigateToCreateFocus() }
        )

    }
}