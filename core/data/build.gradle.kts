plugins {
    id("habithaven.android.library")
    id("habithaven.android.hilt")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.raduleu.habithaven.core.data"
}

dependencies {
    implementation(project(":core:model"))

    // Room (Database)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Coroutines (Useful for background DB work)
    implementation(libs.kotlinx.coroutines.android)

    // Room Testing Helper (CRITICAL)
    androidTestImplementation(libs.androidx.room.testing)

    // Coroutines Testing
    androidTestImplementation(libs.kotlinx.coroutines.test)
}