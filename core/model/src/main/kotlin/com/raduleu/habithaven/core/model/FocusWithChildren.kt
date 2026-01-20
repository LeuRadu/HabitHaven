package com.raduleu.habithaven.core.model

data class FocusWithChildren(
    val focus: Focus,
    val tasks: List<Task>,
    val habits: List<Habit>
)
