package jp.riawithapps.riamusicplayer.ui.player

import androidx.fragment.app.Fragment
import jp.riawithapps.riamusicplayer.ui.databinding.FragmentPlayerBinding
import jp.riawithapps.riamusicplayer.ui.util.repeatCollectOnStarted
import jp.riawithapps.riamusicplayer.usecase.player.PlayerData
import jp.riawithapps.riamusicplayer.usecase.player.PlayerMetaData

fun FragmentPlayerBinding.bind(fragment: Fragment, playerViewModel: PlayerViewModel) {
    playerViewModel.metaData.repeatCollectOnStarted(fragment) { metaData ->
        this.infoTitle.text = metaData.title
        this.seekBarDuration.text = metaData.getDurationText()
    }
    playerViewModel.playerData.repeatCollectOnStarted(fragment) { playerData ->
        this.seekBarCurrentTime.text = playerData.getCurrentTimeText()
    }
}

private fun PlayerMetaData.getDurationText() = this.duration.toString()

private fun PlayerData.getCurrentTimeText() = this.currentTime.toString()
