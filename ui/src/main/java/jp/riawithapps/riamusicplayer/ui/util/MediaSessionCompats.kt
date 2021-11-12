package jp.riawithapps.riamusicplayer.ui.util

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.ExoPlayer
import jp.riawithapps.riamusicplayer.usecase.player.PlayerData

/**
 * プレイヤーの状態をMediaSessionに設定
 *
 * @param exoPlayer
 */
fun MediaSessionCompat.setExoPlayerState(exoPlayer: ExoPlayer) {
    val state = PlaybackStateCompat.Builder().apply {
        // 取り扱う操作。とりあえず 再生準備 再生 一時停止 シーク を扱うようにする。書き忘れると何も起きない
        setActions(
            PlaybackStateCompat.ACTION_PREPARE
                    or PlaybackStateCompat.ACTION_PLAY
                    or PlaybackStateCompat.ACTION_PAUSE
                    or PlaybackStateCompat.ACTION_STOP
                    or PlaybackStateCompat.ACTION_SEEK_TO
        )
        // 再生してるか。ExoPlayerを参照
        val state = if (exoPlayer.isPlaying) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED
        // 位置
        val position = exoPlayer.currentPosition
        // 再生状態を更新
        setState(state, position, 1.0f) // 最後は再生速度
    }.build()
    this.setPlaybackState(state)
}

/**
 * プレイヤーのメタデータをMediaSessionに設定
 *
 * @param exoPlayer
 */
fun MediaSessionCompat.setExoPlayerMetaData(exoPlayer: ExoPlayer, playerData: PlayerData) {
    val mediaMetadataCompat = MediaMetadataCompat.Builder().apply {
        putString(MediaMetadataCompat.METADATA_KEY_TITLE, playerData.title)
        putString(MediaMetadataCompat.METADATA_KEY_ARTIST, playerData.artist)
        putLong(
            MediaMetadataCompat.METADATA_KEY_DURATION,
            exoPlayer.duration
        )
    }.build()

    this.setMetadata(mediaMetadataCompat)
}