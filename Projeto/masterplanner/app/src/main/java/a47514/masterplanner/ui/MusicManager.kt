package a47514.masterplanner.ui

import android.content.Context
import android.media.MediaPlayer
import a47514.masterplanner.R

object MusicManager {

    private var mediaPlayer: MediaPlayer? = null

    fun start(context: Context) {
        if (mediaPlayer != null) return  // already playing
        mediaPlayer = MediaPlayer.create(context, R.raw.background_music).apply {
            isLooping = true
            setVolume(0.4f, 0.4f)  // subtle background volume
            start()
        }
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun resume() {
        mediaPlayer?.takeIf { !it.isPlaying }?.start()
    }

    fun release() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}