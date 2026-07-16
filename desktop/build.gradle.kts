plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
    }
    
    sourceSets {
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(project(":shared"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.metrolist.music.desktop.MainKt"
    }
}
