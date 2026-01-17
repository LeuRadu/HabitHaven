plugins {
    id("habithaven.android.library")
    id("habithaven.android.library.compose")
}

android {
    namespace = "com.raduleu.habithaven.core.designsystem"
}
dependencies {
    implementation(libs.androidx.core)
    implementation(libs.androidx.core.ktx)
}
