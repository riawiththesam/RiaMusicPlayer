package jp.riawithapps.riamusicplayer.ui.player

import android.widget.SeekBar
import androidx.fragment.app.Fragment
import jp.riawithapps.riamusicplayer.ui.databinding.FragmentPlayerBinding
import jp.riawithapps.riamusicplayer.ui.util.*
import jp.riawithapps.riamusicplayer.usecase.player.PlayerData

fun FragmentPlayerBinding.bind(fragment: Fragment, playerViewModel: PlayerViewModel) {
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

    playerViewModel.playerData.repeatCollectOnStarted(fragment) { playerData ->
        this.infoTitle.text = playerData.title
        this.seekBarDuration.text = playerData.getDurationText()
        this.seekBarUI.max = playerData.duration.seconds.toInt()
        this.seekBarCurrentTime.text = playerData.getCurrentTimeText()
        if (enableAutoProgress) this.seekBarUI.progress = playerData.currentTime.seconds.toInt()
    }
}

private fun PlayerData.getDurationText() = duration.getPatternFunction()(duration)

private fun PlayerData.getCurrentTimeText() = duration.getPatternFunction()(currentTime)
