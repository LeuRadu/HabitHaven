package com.raduleu.habithaven.feature.focus

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val CREATE_FOCUS_ROUTE = "create_focus_route"

fun NavController.navigateToCreateFocus(navOptions: NavOptions? = null) {
    this.navigate(CREATE_FOCUS_ROUTE, navOptions)
}

fun NavGraphBuilder.createFocusScreen(
    onBackClick: () -> Unit
) {
    composable(route = CREATE_FOCUS_ROUTE) {
        CreateFocusRoute(onBackClick = onBackClick)
    }
}