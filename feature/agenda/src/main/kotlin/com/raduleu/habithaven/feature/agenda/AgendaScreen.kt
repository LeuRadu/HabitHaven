package com.raduleu.habithaven.feature.agenda

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raduleu.habithaven.core.model.Focus
import com.raduleu.habithaven.core.model.FocusWithChildren
import com.raduleu.habithaven.feature.agenda.ui.FocusCard

@Composable
fun AgendaRoute(
    onAddFocusButtonClick: () -> Unit,
    viewModel: AgendaViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onAddFocusButtonClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create Focus"
                    )
            }
        }
    ) { screenPadding ->

        AgendaScreen(
            modifier = Modifier.padding(screenPadding),
            uiState = uiState,
        )
    }
}

@Composable
fun AgendaScreen(
    modifier: Modifier,
    uiState: AgendaUiState,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is AgendaUiState.Loading -> {
                CircularProgressIndicator()
            }
            is AgendaUiState.Empty -> {
                Text(
                    text = "No active habits found for today.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            is AgendaUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(uiState.focuses) { index, focusWithChildren ->

                        FocusCard(
                            focus = focusWithChildren.focus,
                            tasks = focusWithChildren.tasks,
                            habits = focusWithChildren.habits
                        )

                        // Add Separator only if it's NOT the last item
                        if (index < uiState.focuses.lastIndex) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(2.dp)
                                    .background(Color.Black)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AgendaScreenPreview() {
    val focuses = listOf(
        FocusWithChildren(
            focus = Focus(
                id = "1",
                name = "Work",
                iconName = "work",
                colorIndex = 1,
                sortOrder = 0,
                isArchived = false
            ),
            tasks = emptyList(),
            habits = emptyList()
        ),
        FocusWithChildren(
            focus = Focus(
                id = "2",
                name = "Personal",
                iconName = "person",
                colorIndex = 2,
                sortOrder = 1,
                isArchived = false
            ),
            tasks = emptyList(),
            habits = emptyList()
        ),
        FocusWithChildren(
            focus = Focus(
                id = "3",
                name = "Health",
                iconName = "fitness_center",
                colorIndex = 3,
                sortOrder = 2,
                isArchived = false
            ),
            tasks = emptyList(),
            habits = emptyList()
        )
    )

    MaterialTheme {
        AgendaScreen(
            modifier = Modifier,
            uiState = AgendaUiState.Success(focuses),
        )
    }
}
