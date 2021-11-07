package jp.riawithapps.riamusicplayer.ui.player

import android.widget.SeekBar
import androidx.fragment.app.Fragment
import jp.riawithapps.riamusicplayer.ui.databinding.FragmentPlayerBinding
import jp.riawithapps.riamusicplayer.ui.util.repeatCollectOnStarted
import jp.riawithapps.riamusicplayer.usecase.player.PlayerData
import jp.riawithapps.riamusicplayer.usecase.player.PlayerMetaData

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
        }
    })


    playerViewModel.metaData.repeatCollectOnStarted(fragment) { metaData ->
        this.infoTitle.text = metaData.title
        this.seekBarDuration.text = metaData.getDurationText()
        this.seekBarUI.max = metaData.duration.seconds.toInt()
    }

    playerViewModel.playerData.repeatCollectOnStarted(fragment) { playerData ->
        this.seekBarCurrentTime.text = playerData.getCurrentTimeText()
        if (enableAutoProgress) this.seekBarUI.progress = playerData.currentTime.seconds.toInt()
    }
}

private fun PlayerMetaData.getDurationText() = this.duration.toString()

private fun PlayerData.getCurrentTimeText() = this.currentTime.toString()
