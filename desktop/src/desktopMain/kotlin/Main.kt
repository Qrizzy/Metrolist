package com.metrolist.music.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.metrolist.music.shared.App
import com.metrolist.music.desktop.media.DesktopMediaPlayer
import androidx.compose.runtime.remember

fun main() = application {
    val mediaPlayer = remember { DesktopMediaPlayer() }
    Window(
        onCloseRequest = ::exitApplication,
        title = "Metrolist Desktop"
    ) {
        App(mediaPlayer = mediaPlayer)
    }
}
