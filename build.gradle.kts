// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // We define the versions here using 'apply false'
    // This downloads the plugins but doesn't apply them to the root project itself
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
}