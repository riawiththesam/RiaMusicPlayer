package jp.riawithapps.riamusicplayer.usecase.player

import jp.riawithapps.riamusicplayer.usecase.music.MusicId
import jp.riawithapps.riamusicplayer.usecase.music.MusicRepository
import jp.riawithapps.riamusicplayer.usecase.util.singleUnitFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.threeten.bp.Duration

interface PlayerUseCase {
    val playerData: StateFlow<PlayerData>

    fun setMetaData(musicId: MusicId, duration: Duration): Flow<Unit>
    fun setPlayerData(currentTime: Duration): Flow<Unit>
}

data class PlayerData(
    val title: String,
    val albumTitle: String,
    val artist: String,
    val duration: Duration,
    val currentTime: Duration,
) {
    companion object {
        val EMPTY = PlayerData(
            "",
            "",
            "",
            Duration.ZERO,
            Duration.ZERO,
        )
    }
}

class PlayerInteractor(
    private val musicRepository: MusicRepository,
) : PlayerUseCase {
    override val playerData = MutableStateFlow(PlayerData.EMPTY)

    override fun setMetaData(musicId: MusicId, duration: Duration) = singleUnitFlow {
        val scanResult = musicRepository.scan()
        val music = scanResult.musicList.firstOrNull { it.id == musicId } ?: return@singleUnitFlow
        playerData.value = PlayerData.EMPTY.copy(
            title = music.title,
            albumTitle = music.albumTitle,
            artist = music.artist,
            duration = duration
        )
    }

    override fun setPlayerData(currentTime: Duration) = singleUnitFlow {
        playerData.value = playerData.value.copy(currentTime = currentTime)
    }
}

