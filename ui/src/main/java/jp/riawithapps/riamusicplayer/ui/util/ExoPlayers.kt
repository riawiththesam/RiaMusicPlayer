package jp.riawithapps.riamusicplayer.ui.util

import com.google.android.exoplayer2.ExoPlayer
import org.threeten.bp.Duration

fun ExoPlayer.getCurrentTimeDuration(): Duration = Duration.ofMillis(this.currentPosition)
fun ExoPlayer.getDurationDuration(): Duration = Duration.ofMillis(this.duration)