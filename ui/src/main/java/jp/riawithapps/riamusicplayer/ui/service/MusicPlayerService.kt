package jp.riawithapps.riamusicplayer.ui.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import jp.riawithapps.riamusicplayer.ui.R
import jp.riawithapps.riamusicplayer.ui.util.*
import jp.riawithapps.riamusicplayer.usecase.music.MusicId
import jp.riawithapps.riamusicplayer.usecase.music.toMusicId
import jp.riawithapps.riamusicplayer.usecase.player.PlayerUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/** onLoadChildrenでparentIdに入ってくる。Android 11のメディアの再開の場合はこの値 */
private const val ROOT_RECENT = "root_recent"

/** onLoadChildrenでparentIdに入ってくる。[ROOT_RECENT]以外の場合 */
private const val ROOT = "root"

class MusicPlayerService : MediaBrowserServiceCompat() {
    private val scope = createCoroutineScope()

    private val playerUseCase by inject<PlayerUseCase>()

    private lateinit var onDestroyFunc: () -> Unit

    override fun onCreate() {
        super.onCreate()

        val mediaSessionCompat = MediaSessionCompat(this, "media_session")
        sessionToken = mediaSessionCompat.sessionToken

        var exoPlayer: ExoPlayer? = null

        mediaSessionCompat.setCallback(object : MediaSessionCompat.Callback() {
            override fun onPlayFromUri(uri: Uri?, extras: Bundle?) {
                super.onPlayFromUri(uri, extras)
                val musicId = extras?.toMusicId() ?: return
                exoPlayer?.release()
                exoPlayer = createPlayer(musicId, mediaSessionCompat)
            }

            override fun onPause() {
                super.onPause()
                exoPlayer?.pause()
            }

            override fun onPlay() {
                super.onPlay()
                exoPlayer?.play()
            }

            override fun onStop() {
                super.onStop()
                exoPlayer?.stop()
                stopSelf()
            }

            override fun onSeekTo(pos: Long) {
                super.onSeekTo(pos)
                exoPlayer?.seekTo(pos)
            }
        })

        scope.launch(Dispatchers.Main) {
            playerUseCase.playerData.collect {
                val player = exoPlayer ?: return@collect
                mediaSessionCompat.setExoPlayerMetaData(player, it)
            }
        }

        flow {
            while (true) {
                val player = exoPlayer
                if (player != null) {
                    emit(player.getCurrentTimeDuration())
                }
                delay(200)
            }
        }.flowOn(Dispatchers.Main).onEach {
            playerUseCase.setPlayerData(it).launchIn(scope)
        }.launchIn(scope)

        onDestroyFunc = {
            scope.cancel()
            mediaSessionCompat.release()
            exoPlayer?.release()
            exoPlayer = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroyFunc()
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

    /** 通知を表示する */
    private fun showNotification(exoPlayer: ExoPlayer, mediaSessionCompat: MediaSessionCompat) {
        // 通知表示
        startForeground(84, createNotification(this, mediaSessionCompat.sessionToken, exoPlayer.isPlaying))
    }

    private fun createPlayer(musicId: MusicId, mediaSessionCompat: MediaSessionCompat): ExoPlayer {
        return SimpleExoPlayer.Builder(this@MusicPlayerService)
            .build()
            .also { exoPlayer ->
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
                        mediaSessionCompat.setExoPlayerState(exoPlayer)
                        playerUseCase.setMetaData(
                            musicId,
                            exoPlayer.getDurationDuration(),
                        ).launchIn(scope)
                        showNotification(exoPlayer, mediaSessionCompat)
                    }
                })

                exoPlayer.playWhenReady = true
                val dataSource = DefaultDataSourceFactory(this@MusicPlayerService)
                val mediaSource = ProgressiveMediaSource.Factory(dataSource)
                    .createMediaSource(
                        MediaItem.fromUri(
                            ContentUris.withAppendedId(
                                musicId.getUri(),
                                musicId.rawValue
                            )
                        )
                    )
                exoPlayer.addMediaSource(mediaSource)
                exoPlayer.prepare()
            }
    }
}

@Suppress("deprecation")
private fun createNotification(
    context: Context,
    sessionToken: MediaSessionCompat.Token,
    isPlaying: Boolean,
): Notification {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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
        NotificationCompat.Builder(context, channelId)
    } else {
        NotificationCompat.Builder(context)
    }
    return notification.apply {
        setStyle(
            androidx.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(sessionToken).setShowActionsInCompactView(0)
        )
        setSmallIcon(R.drawable.ic_play_arrow_black_24dp)
        // 通知領域に置くボタン
        if (isPlaying) {
            addAction(
                NotificationCompat.Action(
                    R.drawable.ic_outline_pause_24,
                    "一時停止",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        context,
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
                        context,
                        PlaybackStateCompat.ACTION_PLAY
                    )
                )
            )
        }
        addAction(
            NotificationCompat.Action(
                R.drawable.ic_outline_stop_24,
                "停止",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_STOP
                )
            )
        )
    }.build()
}