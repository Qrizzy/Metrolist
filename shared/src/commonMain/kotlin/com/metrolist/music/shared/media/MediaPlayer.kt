package com.metrolist.music.shared.media

interface MediaPlayer {
    fun play(url: String)
    fun pause()
    fun resume()
    fun stop()
    fun seekTo(positionMs: Long)
    
    val isPlaying: Boolean
    val currentPositionMs: Long
}
