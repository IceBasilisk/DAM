package a47514.masterplanner.data

import a47514.masterplanner.R
import android.content.Context
import android.media.MediaPlayer

/**
 * Manages the background music.
 */
object MusicManager {

    private var mediaPlayer: MediaPlayer? = null

    /**
     * Starts playing the background music.
     */
    fun start(context: Context) {
        if (mediaPlayer != null) return  // already playing
        mediaPlayer = MediaPlayer.create(context, R.raw.background_music).apply {
            isLooping = true
            setVolume(0.4f, 0.4f)
            start()
        }
    }

    /**
     * Pauses the background music.
     */
    fun pause() {
        mediaPlayer?.pause()
    }

    /**
     * Resumes the background music.
     */
    fun resume() {
        mediaPlayer?.takeIf { !it.isPlaying }?.start()
    }

    /**
     * Stops and releases the background music.
     */
    fun release() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}