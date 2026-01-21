package com.raduleu.habithaven

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.raduleu.habithaven.core.designsystem.theme.HabitHavenTheme // Assuming you have a theme
import com.raduleu.habithaven.ui.HabitHavenApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HabitHavenTheme {
                HabitHavenApp()
            }
        }
    }
}