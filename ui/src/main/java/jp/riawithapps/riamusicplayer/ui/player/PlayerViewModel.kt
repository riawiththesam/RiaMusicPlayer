package jp.riawithapps.riamusicplayer.ui.player

import androidx.lifecycle.ViewModel
import jp.riawithapps.riamusicplayer.usecase.player.PlayerUseCase

class PlayerViewModel(
    playerUseCase: PlayerUseCase,
) : ViewModel() {
    val metaData = playerUseCase.metaData
    val playerData = playerUseCase.playerData
}
