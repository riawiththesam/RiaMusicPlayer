package jp.riawithapps.riamusicplayer.usecase.player

import jp.riawithapps.riamusicplayer.usecase.music.MusicId
import jp.riawithapps.riamusicplayer.usecase.music.MusicRepository
import jp.riawithapps.riamusicplayer.usecase.util.singleUnitFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.threeten.bp.Duration

interface PlayerUseCase {
    val metaData: StateFlow<PlayerMetaData>
    val playerData: StateFlow<PlayerData>

    fun setMetaData(musicId: MusicId, duration: Duration): Flow<Unit>
    fun setPlayerData(currentTime: Duration): Flow<Unit>
}

data class PlayerMetaData(
    val title: String,
    val albumTitle: String,
    val artist: String,
    val duration: Duration,
)

data class PlayerData(
    val currentTime: Duration,
)

class PlayerInteractor(
    private val musicRepository: MusicRepository,
) : PlayerUseCase {
    override val metaData = MutableStateFlow(PlayerMetaData("", "", "", Duration.ZERO))
    override val playerData = MutableStateFlow(PlayerData(Duration.ZERO))

    override fun setMetaData(musicId: MusicId, duration: Duration) = singleUnitFlow {
        val scanResult = musicRepository.scan()
        val music = scanResult.musicList.firstOrNull { it.id == musicId } ?: return@singleUnitFlow
        metaData.value = PlayerMetaData(
            title = music.title,
            albumTitle = music.albumTitle,
            artist = music.artist,
            duration = duration,
        )
    }

    override fun setPlayerData(currentTime: Duration) = singleUnitFlow {
        playerData.value = PlayerData(currentTime = currentTime)
    }
}

