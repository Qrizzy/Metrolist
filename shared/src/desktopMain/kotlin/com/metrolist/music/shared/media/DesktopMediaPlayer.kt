package com.metrolist.music.shared.media

// TODO: Integrate Desktop Audio framework (e.g. JavaFX Media, VLCJ, or Java Sound API)
class DesktopMediaPlayer : MediaPlayer {
    override var isPlaying: Boolean = false
        private set
        
    override var currentPositionMs: Long = 0L
        private set

    override fun play(url: String) {
        println("DesktopMediaPlayer: Playing URL $url")
        isPlaying = true
    }

    override fun pause() {
        println("DesktopMediaPlayer: Paused")
        isPlaying = false
    }

    override fun resume() {
        println("DesktopMediaPlayer: Resumed")
        isPlaying = true
    }

    override fun stop() {
        println("DesktopMediaPlayer: Stopped")
        isPlaying = false
    }

    override fun seekTo(positionMs: Long) {
        println("DesktopMediaPlayer: Seeking to $positionMs")
        currentPositionMs = positionMs
    }
}
