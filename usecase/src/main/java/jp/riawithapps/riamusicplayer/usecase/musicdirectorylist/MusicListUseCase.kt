package jp.riawithapps.riamusicplayer.usecase.musicdirectorylist

import jp.riawithapps.riamusicplayer.usecase.music.MusicFile
import jp.riawithapps.riamusicplayer.usecase.music.MusicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface MusicListUseCase {
    fun load(): Flow<MusicListLoadResult>
    fun scan(): Flow<MusicListLoadResult>
}

class MusicListLoadResult(
    val list: List<MusicFile>,
)

class MusicDirectoryListInteractor(
    private val musicListRepository: MusicRepository,
) : MusicListUseCase {
    override fun load(): Flow<MusicListLoadResult> = flow {
        emit(MusicListLoadResult(emptyList()))
    }

    override fun scan(): Flow<MusicListLoadResult> = flow {
        emit(MusicListLoadResult(musicListRepository.scan().musicList))
    }
}