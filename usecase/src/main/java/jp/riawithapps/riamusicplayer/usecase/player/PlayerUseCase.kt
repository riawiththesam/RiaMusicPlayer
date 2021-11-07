package jp.riawithapps.riamusicplayer.usecase.player

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

interface PlayerUseCase {
    val metaData: StateFlow<PlayerMetaData>
    fun setMetaData(next: PlayerMetaData): Flow<Unit>
}

data class PlayerMetaData(
    val title: String,
    val duration: Long,
)

class PlayerInteractor : PlayerUseCase {
    override val metaData = MutableStateFlow(PlayerMetaData("", 0))

    override fun setMetaData(next: PlayerMetaData) = flow {
        metaData.emit(next)
        emit(Unit)
    }
}

