package com.raduleu.habithaven.feature.focus

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun FocusFormRoute(
    onBackClick: () -> Unit,
    viewModel: FocusFormViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onBackClick()
            viewModel.onFocusSaved()
        }
    }

    FocusFormScreen(
        uiState = uiState,
        onNameChange = viewModel::updateName,
        onIconSelect = viewModel::updateIconName,
        onColorSelect = viewModel::updateColorIndex,
        onSaveClick = viewModel::saveFocus,
        onBackClick = onBackClick,
        onDeleteClick = viewModel::onDeleteClick,
        onDeleteConfirm = viewModel::confirmDelete,
        onDeleteDismiss = viewModel::onDeleteDismiss
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FocusFormScreen(
    uiState: FocusFormUiState,
    onNameChange: (String) -> Unit,
    onIconSelect: (String) -> Unit,
    onColorSelect: (Int) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDeleteConfirm: () -> Unit,
    onDeleteDismiss: () -> Unit,
) {
    val titleText = if (uiState.isEditing) "Edit Focus" else "New Focus"
    val buttonText = if (uiState.isEditing) "Save changes" else "Create Focus"

    if (uiState.isDeleteDialogVisible) {
        AlertDialog(
            onDismissRequest = onDeleteDismiss,
            title = { Text("Archive Focus?") },
            text = { Text("This will hide the focus from your main agenda. You can restore it later from settings.") },
            confirmButton = {
                TextButton(onClick = onDeleteConfirm) {
                    Text("Archive", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = onDeleteDismiss) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titleText) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Name Input
            OutlinedTextField(
                value = uiState.name,
                onValueChange = onNameChange,
                label = { Text("Focus Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Icon Selection (Placeholder Data)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Icon", style = MaterialTheme.typography.titleMedium)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val icons = listOf("category", "work", "fitness", "code", "book")
                    items(icons) { icon ->
                        FilterChip(
                            selected = uiState.iconName == icon,
                            onClick = { onIconSelect(icon) },
                            label = { Text(icon) }
                        )
                    }
                }
            }

            // Color Selection (Placeholder Data)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Color", style = MaterialTheme.typography.titleMedium)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    // TODO: replace with color palette
                    val colors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta)

                    items(colors.indices.toList()) { index ->
                        val isSelected = uiState.colorIndex == index
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(colors[index])
                                .clickable { onColorSelect(index) }
                                .then(
                                    if (isSelected) Modifier.border(3.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                                    else Modifier
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onSaveClick,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.name.isNotBlank() && !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(buttonText)
                    }
                }

                if (uiState.isEditing) {
                    OutlinedButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Delete Focus")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewFocusFormScreen() {
    FocusFormScreen(
        uiState = FocusFormUiState(name = "My Focus", iconName = "code", colorIndex = 1),
        onNameChange = {},
        onIconSelect = {},
        onColorSelect = {},
        onSaveClick = {},
        onBackClick = {},
        onDeleteClick = {},
        onDeleteConfirm = {},
        onDeleteDismiss = {}
    )
}