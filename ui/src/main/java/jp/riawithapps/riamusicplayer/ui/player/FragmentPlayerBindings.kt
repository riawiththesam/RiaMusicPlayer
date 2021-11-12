package jp.riawithapps.riamusicplayer.ui.player

import android.widget.SeekBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import jp.riawithapps.riamusicplayer.ui.databinding.FragmentPlayerBinding
import jp.riawithapps.riamusicplayer.ui.util.getPatternFunction
import jp.riawithapps.riamusicplayer.ui.util.repeatCollectOnStarted
import jp.riawithapps.riamusicplayer.usecase.player.PlaybackState
import jp.riawithapps.riamusicplayer.usecase.player.PlayerData

fun FragmentPlayerBinding.bind(fragment: Fragment, playerViewModel: PlayerViewModel) {
    // シークバー
    var enableAutoProgress = true
    this.seekBarUI.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            enableAutoProgress = false
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            enableAutoProgress = true
            val bar = seekBar ?: return
            playerViewModel.onSeek(bar.progress / bar.max.toDouble())
        }
    })

    // メイン操作パネル
    this.mainControlPlay.setOnClickListener { playerViewModel.onClickPlay() }
    this.mainControlPause.setOnClickListener { playerViewModel.onClickPause() }

    // プレイヤーの状態を画面に反映
    playerViewModel.playerData.repeatCollectOnStarted(fragment) { playerData ->
        this.infoTitle.text = playerData.title
        this.seekBarDuration.text = playerData.getDurationText()
        this.seekBarUI.max = playerData.duration.seconds.toInt()
        this.seekBarCurrentTime.text = playerData.getCurrentTimeText()
        if (enableAutoProgress) this.seekBarUI.progress = playerData.currentTime.seconds.toInt()

        this.mainControlPlay.isVisible = playerData.playbackState == PlaybackState.Paused
        this.mainControlPause.isVisible = playerData.playbackState == PlaybackState.Playing
    }
}

private fun PlayerData.getDurationText() = duration.getPatternFunction()(duration)

private fun PlayerData.getCurrentTimeText() = duration.getPatternFunction()(currentTime)
