plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }
    
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
            }
        }
        val androidMain by getting
        val desktopMain by getting
    }
}

android {
    namespace = "com.metrolist.music.shared"
    compileSdk = 37
    defaultConfig {
        minSdk = 26
    }
}
