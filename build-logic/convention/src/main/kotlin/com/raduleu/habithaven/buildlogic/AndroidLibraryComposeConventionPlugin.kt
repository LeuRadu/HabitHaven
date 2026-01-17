package com.raduleu.habithaven.buildlogic

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            val extension = extensions.getByType<LibraryExtension>()
            extension.apply {
                buildFeatures {
                    compose = true
                }
                // Note: We don't need to set compileOptions/kotlinOptions here
                // because the base 'habithaven.android.library' plugin already does it.
            }

            // Add Compose dependencies automatically
            dependencies {
                val bom = libs.findLibrary("androidx.compose.bom").get()
                "implementation"(platform(bom))
                "androidTestImplementation"(platform(bom))

                "implementation"(libs.findLibrary("androidx.ui.tooling.preview").get())
                "implementation"(libs.findLibrary("androidx.material3").get())
                "debugImplementation"(libs.findLibrary("androidx.ui.tooling").get())
            }
        }
    }
}