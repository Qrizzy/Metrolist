package com.metrolist.music.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.metrolist.music.shared.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Metrolist Desktop"
    ) {
        App()
    }
}
