package jp.riawithapps.riamusicplayer.ui.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import jp.riawithapps.riamusicplayer.ui.R

/** onLoadChildrenでparentIdに入ってくる。Android 11のメディアの再開の場合はこの値 */
private const val ROOT_RECENT = "root_recent"

/** onLoadChildrenでparentIdに入ってくる。[ROOT_RECENT]以外の場合 */
private const val ROOT = "root"

class MusicPlayerService : MediaBrowserServiceCompat() {
    private lateinit var notificationManager: NotificationManager
    private lateinit var exoPlayer: SimpleExoPlayer
    private lateinit var mediaSessionCompat: MediaSessionCompat

    override fun onCreate() {
        super.onCreate()

        // 通知出すのに使う
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // ExoPlayer用意
        exoPlayer = SimpleExoPlayer.Builder(this).build()

        // MediaSession用意
        mediaSessionCompat = MediaSessionCompat(this, "media_session")

        // Set the session's token so that client activities can communicate with it.
        sessionToken = mediaSessionCompat.sessionToken

        mediaSessionCompat.setCallback(object : MediaSessionCompat.Callback() {
            override fun onPlayFromUri(uri: Uri?, extras: Bundle?) {
                super.onPlayFromUri(uri, extras)
                if (uri == null) return
                val dataSource = DefaultDataSourceFactory(this@MusicPlayerService)
                val mediaSource = ProgressiveMediaSource.Factory(dataSource)
                    .createMediaSource(MediaItem.fromUri(uri))
                exoPlayer.playWhenReady = true
                exoPlayer.addMediaSource(mediaSource)
                exoPlayer.prepare()
            }

            override fun onPause() {
                super.onPause()
                exoPlayer.pause()
            }

            override fun onPlay() {
                super.onPlay()
                exoPlayer.play()
            }

            override fun onStop() {
                super.onStop()
                exoPlayer.stop()
                stopSelf()
            }
        })

        // ExoPlayerの再生状態が更新されたときも通知を更新する
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        startThisService()
                    }
                    Player.STATE_ENDED -> {
                        stopSelf()
                    }
                    else -> {
                    }
                }
                updateState()
                showNotification()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSessionCompat.release()
        exoPlayer.release()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?,
    ): BrowserRoot {
        // 外のパッケージからのアクセスを遮断したりする必要がある？
        // 最後の曲をリクエストしている場合はtrue
        val isRequestRecentMusic = rootHints?.getBoolean(BrowserRoot.EXTRA_RECENT) ?: false
        // BrowserRootに入れる値を変える
        val rootPath = if (isRequestRecentMusic) ROOT_RECENT else ROOT
        return BrowserRoot(rootPath, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        if (parentId == ROOT_RECENT) {
            // 動画情報いれる
            result.sendResult(mutableListOf())
        } else {
            result.sendResult(mutableListOf())
        }
    }

    /**
     * [onLoadChildren]で返すアイテムを作成する
     * */
    /*
    @Suppress("SameParameterValue")
    private fun createMediaItem(
        videoId: String,
        title: String,
        subTitle: String? = null,
    ): MediaBrowserCompat.MediaItem {
        val mediaDescriptionCompat = MediaDescriptionCompat.Builder().apply {
            setTitle(title)
            if (subTitle != null) setSubtitle(subTitle)
            setMediaId(videoId)
        }.build()
        return MediaBrowserCompat.MediaItem(
            mediaDescriptionCompat,
            MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
        )
    }
     */

    /** フォアグラウンドサービスを起動する */
    private fun startThisService() {
        val playlistPlayServiceIntent = Intent(this, MusicPlayerService::class.java)
        // 起動
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(playlistPlayServiceIntent)
        } else {
            startService(playlistPlayServiceIntent)
        }
    }

    /**
     * 再生状態とメタデータを設定する。今回はメタデータはハードコートする
     *
     * MediaSessionのsetCallBackで扱う操作([MediaSessionCompat.Callback.onPlay]など)も[PlaybackStateCompat.Builder.setState]に書かないと何も起きない
     * */
    private fun updateState() {
        val stateBuilder = PlaybackStateCompat.Builder().apply {
            // 取り扱う操作。とりあえず 再生準備 再生 一時停止 シーク を扱うようにする。書き忘れると何も起きない
            setActions(
                PlaybackStateCompat.ACTION_PREPARE
                        or PlaybackStateCompat.ACTION_PLAY
                        or PlaybackStateCompat.ACTION_PAUSE
                        or PlaybackStateCompat.ACTION_STOP
                        or PlaybackStateCompat.ACTION_SEEK_TO
            )
            // 再生してるか。ExoPlayerを参照
            val state =
                if (exoPlayer.isPlaying) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED
            // 位置
            val position = exoPlayer.currentPosition
            // 再生状態を更新
            setState(state, position, 1.0f) // 最後は再生速度
        }.build()
        mediaSessionCompat.setPlaybackState(stateBuilder)
        // メタデータの設定
        val duration = 288L // 再生時間
        val mediaMetadataCompat = MediaMetadataCompat.Builder().apply {
            // Android 11 の MediaSession で使われるやつ
            putString(MediaMetadataCompat.METADATA_KEY_TITLE, "音楽のタイトル")
            putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "音楽のアーティスト")
            putLong(
                MediaMetadataCompat.METADATA_KEY_DURATION,
                duration * 1000
            ) // これあるとAndroid 10でシーク使えます
        }.build()
        mediaSessionCompat.setMetadata(mediaMetadataCompat)
    }

    /** 通知を表示する */
    private fun showNotification() {
        // 通知を作成。通知チャンネルのせいで長い
        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 通知チャンネル
            val channelId = "playlist_play"
            val notificationChannel =
                NotificationChannel(channelId, "音楽コントローラー", NotificationManager.IMPORTANCE_LOW)
            if (notificationManager.getNotificationChannel(channelId) == null) {
                // 登録
                notificationManager.createNotificationChannel(notificationChannel)
            }
            NotificationCompat.Builder(this, channelId)
        } else {
            NotificationCompat.Builder(this)
        }
        notification.apply {
            setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSessionCompat.sessionToken).setShowActionsInCompactView(0)
            )
            setSmallIcon(R.drawable.ic_play_arrow_black_24dp)
            // 通知領域に置くボタン
            if (exoPlayer.isPlaying) {
                addAction(
                    NotificationCompat.Action(
                        R.drawable.ic_play_arrow_black_24dp,
                        "一時停止",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            this@MusicPlayerService,
                            PlaybackStateCompat.ACTION_PAUSE
                        )
                    )
                )
            } else {
                addAction(
                    NotificationCompat.Action(
                        R.drawable.ic_play_arrow_black_24dp,
                        "再生",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            this@MusicPlayerService,
                            PlaybackStateCompat.ACTION_PLAY
                        )
                    )
                )
            }
            addAction(
                NotificationCompat.Action(
                    R.drawable.ic_play_arrow_black_24dp,
                    "停止",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        this@MusicPlayerService,
                        PlaybackStateCompat.ACTION_STOP
                    )
                )
            )
        }
        // 通知表示
        startForeground(84, notification.build())
    }
}