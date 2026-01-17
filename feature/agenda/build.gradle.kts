plugins {
    id("habithaven.android.library")
    id("habithaven.android.library.compose")
    id("habithaven.android.hilt")
}

android {
    namespace = "com.raduleu.habithaven.feature.agenda"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:data"))

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.hilt.navigation.compose)
}