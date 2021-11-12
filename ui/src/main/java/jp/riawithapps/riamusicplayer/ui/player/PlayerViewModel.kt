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
    val playerData = playerUseCase.playerData

    private val _event = MutableSharedFlow<PlayerEvent>()
    val event: SharedFlow<PlayerEvent> = _event

    fun onSeek(ratio: Double) {
        val durationMillis = playerData.value.duration.toMillis()
        val to = Duration.ofMillis((durationMillis * ratio).roundToLong())
        _event.emit(scope, PlayerEvent.Seek(to))
    }

    fun onClickPause() {
        _event.emit(scope, PlayerEvent.Pause)
    }

    fun onClickPlay() {
        _event.emit(scope, PlayerEvent.Play)
    }
}

sealed class PlayerEvent {
    class Seek(val to: Duration) : PlayerEvent()
    object Pause: PlayerEvent()
    object Play: PlayerEvent()
}