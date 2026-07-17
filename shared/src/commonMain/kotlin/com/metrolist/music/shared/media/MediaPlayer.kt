package com.metrolist.music.shared.media

interface MediaPlayer {
    fun play(url: String)
    fun pause()
    fun resume()
    fun stop()
    fun seekTo(positionMs: Long)
    fun setVolume(volume: Float)
    
    val isPlaying: Boolean
    val currentPositionMs: Long
    val lengthMs: Long
    val volume: Float
}
