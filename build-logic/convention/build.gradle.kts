import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.raduleu.habithaven.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    // Removed the missing Compose dependency for now
}

// Fix for the deprecation warning: use 'compilerOptions' instead of 'kotlinOptions'
tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "habithaven.android.application"
            implementationClass = "com.raduleu.habithaven.buildlogic.AndroidApplicationConventionPlugin"
        }

        register("androidLibrary") {
            id = "habithaven.android.library"
            implementationClass = "com.raduleu.habithaven.buildlogic.AndroidLibraryConventionPlugin"
        }

        register("androidHilt") {
            id = "habithaven.android.hilt"
            implementationClass = "com.raduleu.habithaven.buildlogic.AndroidHiltConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = "habithaven.android.library.compose"
            implementationClass = "com.raduleu.habithaven.buildlogic.AndroidLibraryComposeConventionPlugin"
        }
    }
}