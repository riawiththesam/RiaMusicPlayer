package jp.riawithapps.riamusicplayer.ui.player

import android.support.v4.media.MediaMetadataCompat
import androidx.lifecycle.ViewModel
import jp.riawithapps.riamusicplayer.ui.util.createScope
import jp.riawithapps.riamusicplayer.ui.util.getDuration
import jp.riawithapps.riamusicplayer.ui.util.getTitle
import jp.riawithapps.riamusicplayer.usecase.player.PlayerMetaData
import jp.riawithapps.riamusicplayer.usecase.player.PlayerUseCase
import kotlinx.coroutines.flow.launchIn

class PlayerViewModel(
    private val playerUseCase: PlayerUseCase,
) : ViewModel() {
    private val scope = createScope()
    val metaData = playerUseCase.metaData

    fun onMetaDataChanged(next: MediaMetadataCompat) {
        playerUseCase.setMetaData(next.toPlayerMetadata())
            .launchIn(scope)
    }
}

private fun MediaMetadataCompat.toPlayerMetadata() = PlayerMetaData(
    title = this.getTitle().toString(),
    duration = this.getDuration(),
)
