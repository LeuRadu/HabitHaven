package com.raduleu.habithaven.core.model

data class Project(
    val id: String,
    val focusId: String,
    val habitChainId: String?,
    val title: String,
    val deadline: Long?,
    val isCompleted: Boolean
)