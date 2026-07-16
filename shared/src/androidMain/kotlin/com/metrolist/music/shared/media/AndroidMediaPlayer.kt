package com.metrolist.music.shared.media

// TODO: Integrate actual ExoPlayer instance here
class AndroidMediaPlayer : MediaPlayer {
    override var isPlaying: Boolean = false
        private set
        
    override var currentPositionMs: Long = 0L
        private set

    override fun play(url: String) {
        // ExoPlayer.setMediaItem(MediaItem.fromUri(url))
        // ExoPlayer.prepare()
        // ExoPlayer.play()
        isPlaying = true
    }

    override fun pause() {
        // ExoPlayer.pause()
        isPlaying = false
    }

    override fun resume() {
        // ExoPlayer.play()
        isPlaying = true
    }

    override fun stop() {
        // ExoPlayer.stop()
        isPlaying = false
    }

    override fun seekTo(positionMs: Long) {
        // ExoPlayer.seekTo(positionMs)
        currentPositionMs = positionMs
    }
}
