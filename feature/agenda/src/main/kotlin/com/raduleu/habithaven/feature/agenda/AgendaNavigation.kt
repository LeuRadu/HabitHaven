package com.raduleu.habithaven.feature.agenda

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val AGENDA_ROUTE = "agenda_route"

fun NavController.navigateToAgenda(navOptions: NavOptions? = null) {
    this.navigate(AGENDA_ROUTE, navOptions)
}

fun NavGraphBuilder.agendaScreen(
    onAddFocusButtonClick: () -> Unit
) {
    composable(route = AGENDA_ROUTE) {
        AgendaRoute(onAddFocusButtonClick = onAddFocusButtonClick)
    }
}