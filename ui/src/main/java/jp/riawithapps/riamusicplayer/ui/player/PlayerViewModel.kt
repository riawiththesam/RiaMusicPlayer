package jp.riawithapps.riamusicplayer.ui.player

import android.support.v4.media.MediaMetadataCompat
import androidx.lifecycle.ViewModel
import jp.riawithapps.riamusicplayer.ui.util.createScope
import jp.riawithapps.riamusicplayer.ui.util.emit
import jp.riawithapps.riamusicplayer.ui.util.getDuration
import jp.riawithapps.riamusicplayer.ui.util.getTitle
import kotlinx.coroutines.flow.MutableStateFlow

class PlayerViewModel : ViewModel() {
    private val scope = createScope()

    val metaData = MutableStateFlow(PlayerMetaData("", ""))

    fun onMetaDataChanged(next: MediaMetadataCompat) {
        metaData.emit(scope, PlayerMetaData(next))
    }
}

data class PlayerMetaData(
    val title: String,
    val duration: String,
) {
    constructor(meta: MediaMetadataCompat) : this(
        title = meta.getTitle().toString(),
        duration = meta.getDuration().toString(),
    )
}