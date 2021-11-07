package jp.riawithapps.riamusicplayer.ui.player

import androidx.lifecycle.ViewModel
import jp.riawithapps.riamusicplayer.ui.util.createScope
import jp.riawithapps.riamusicplayer.ui.util.emit
import jp.riawithapps.riamusicplayer.usecase.player.PlayerUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.threeten.bp.Duration
import kotlin.math.roundToLong

class PlayerViewModel(
    playerUseCase: PlayerUseCase,
) : ViewModel() {
    private val scope = createScope()
    val metaData = playerUseCase.metaData
    val playerData = playerUseCase.playerData

    private val _event = MutableSharedFlow<PlayerEvent>()
    val event: SharedFlow<PlayerEvent> = _event

    fun onSeek(ratio: Double) {
        val durationMillis = metaData.value.duration.toMillis()
        val to = Duration.ofMillis((durationMillis * ratio).roundToLong())
        _event.emit(scope, PlayerEvent.Seek(to))
    }
}

sealed class PlayerEvent {
    class Seek(val to: Duration) : PlayerEvent()
}