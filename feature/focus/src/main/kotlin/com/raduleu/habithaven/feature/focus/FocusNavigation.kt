package com.raduleu.habithaven.feature.focus.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.raduleu.habithaven.feature.focus.FocusFormRoute // Assuming you renamed the Route composable

const val FOCUS_ID_ARG = "focusId"
const val CREATE_FOCUS_ROUTE = "focus_create_route"
const val EDIT_FOCUS_ROUTE = "focus_edit_route/{$FOCUS_ID_ARG}" // "focus_edit_route/{focusId}"

fun NavController.navigateToCreateFocus(navOptions: NavOptions? = null) {
    this.navigate(CREATE_FOCUS_ROUTE, navOptions)
}

fun NavController.navigateToEditFocus(focusId: String, navOptions: NavOptions? = null) {
    val route = "focus_edit_route/$focusId"
    this.navigate(route, navOptions)
}

fun NavGraphBuilder.focusFormScreen(
    onBackClick: () -> Unit
) {
    composable(route = CREATE_FOCUS_ROUTE) {
        FocusFormRoute(onBackClick = onBackClick)
    }

    composable(
        route = EDIT_FOCUS_ROUTE,
        arguments = listOf(
            navArgument(FOCUS_ID_ARG) { type = NavType.StringType }
        )
    ) {
        FocusFormRoute(onBackClick = onBackClick)
    }
}