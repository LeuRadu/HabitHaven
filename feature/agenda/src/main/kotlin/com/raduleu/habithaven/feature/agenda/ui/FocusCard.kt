package com.raduleu.habithaven.feature.agenda.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.raduleu.habithaven.core.model.Focus
import com.raduleu.habithaven.core.model.Habit
import com.raduleu.habithaven.core.model.Task

@Composable
fun FocusCard(
    focus: Focus,
    tasks: List<Task>,
    habits: List<Habit>,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(true) }

    //TODO: replace placeholder color picker with color palette
    val baseColor = when (focus.colorIndex) {
        0 -> Color.Red
        1 -> Color.Blue
        2 -> Color.Green
        3 -> Color.Yellow
        4 -> Color.Magenta
        else -> { Color.Gray }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            // Animate the size change when the list expands/collapses
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .background(
                color = baseColor.copy(alpha = 0.15f), // Very light tint for the whole group
            )
    ) {
        // --- FOCUS HEADER ---
        FocusHeader(
            name = focus.name,
            icon = Icons.Default.Star, // TODO: replace placeholder with actual icon
            color = baseColor,
            isExpanded = isExpanded,
            onToggle = { isExpanded = !isExpanded }
        )

        // --- SUBLIST (Conditional) ---
        if (isExpanded) {
            Column(
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                if (habits.isEmpty()) {
                    EmptyStateText()
                } else {
                    habits.forEach { habit ->
                        Text(text = habit.title)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun FocusHeader(
    name: String,
    icon: ImageVector,
    color: Color,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Box with stronger tint
        Surface(
            color = color.copy(alpha = 0f),
            modifier = Modifier.size(32.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color.copy(alpha = 1f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = if (isExpanded) "Collapse" else "Expand",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun EmptyStateText() {
    Text(
        text = "No tasks for today",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
    )
}
