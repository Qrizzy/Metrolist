package com.metrolist.music.desktop.media

import com.metrolist.music.shared.media.MediaPlayer
import uk.co.caprica.vlcj.factory.MediaPlayerFactory
import uk.co.caprica.vlcj.player.base.MediaPlayer as VlcMediaPlayer
import com.sun.jna.NativeLibrary

class DesktopMediaPlayer : MediaPlayer {
    private var factory: MediaPlayerFactory? = null
    private var mediaPlayer: VlcMediaPlayer? = null

    init {
        try {
            // Force JNA to look in the standard 64-bit VLC directory
            NativeLibrary.addSearchPath("libvlc", "C:\\Program Files\\VideoLAN\\VLC")
            // Also try the 32-bit directory just in case the user installed the 32-bit version on a 32-bit JVM
            NativeLibrary.addSearchPath("libvlc", "C:\\Program Files (x86)\\VideoLAN\\VLC")
            
            factory = MediaPlayerFactory("--http-user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
            mediaPlayer = factory?.mediaPlayers()?.newMediaPlayer()
            mediaPlayer?.audio()?.setVolume(100) // Ensure volume starts at 100%
        } catch (e: Exception) {
            println("VLC not found or failed to load. Please install VLC Media Player (64-bit) for audio playback.")
            e.printStackTrace()
        } catch (e: UnsatisfiedLinkError) {
            println("VLC native libraries not found. Please install VLC Media Player (64-bit) for audio playback.")
            e.printStackTrace()
        } catch (e: Error) {
            println("Error loading VLC: ${e.message}. Please install VLC Media Player (64-bit).")
            e.printStackTrace()
        }
    }

    override fun play(url: String) {
        mediaPlayer?.media()?.play(url) ?: println("Playback failed: VLC is not installed.")
    }

    override fun pause() {
        mediaPlayer?.controls()?.pause()
    }

    override fun resume() {
        mediaPlayer?.controls()?.play()
    }

    override fun stop() {
        mediaPlayer?.controls()?.stop()
    }

    override fun seekTo(positionMs: Long) {
        mediaPlayer?.controls()?.setTime(positionMs)
    }

    override fun setVolume(volume: Float) {
        mediaPlayer?.audio()?.setVolume((volume * 100).toInt())
    }

    override val isPlaying: Boolean
        get() = mediaPlayer?.status()?.isPlaying ?: false

    override val currentPositionMs: Long
        get() = mediaPlayer?.status()?.time() ?: 0L

    override val lengthMs: Long
        get() = mediaPlayer?.status()?.length() ?: 0L

    override val volume: Float
        get() = (mediaPlayer?.audio()?.volume() ?: 100) / 100f
}
