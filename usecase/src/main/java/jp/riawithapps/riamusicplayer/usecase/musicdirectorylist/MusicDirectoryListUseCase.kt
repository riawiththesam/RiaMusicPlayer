package jp.riawithapps.riamusicplayer.usecase.musicdirectorylist

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface MusicDirectoryListUseCase {
    fun load(): Flow<MusicDirectoryListLoadResult>
    fun scan(): Flow<MusicDirectoryListLoadResult>
}

class MusicDirectoryListLoadResult(
    val list: List<String>,
)

class MusicDirectoryListInteractor : MusicDirectoryListUseCase {
    override fun load(): Flow<MusicDirectoryListLoadResult> = flow {
        emit(MusicDirectoryListLoadResult(listOf("Test1", "Test2")))
    }

    override fun scan(): Flow<MusicDirectoryListLoadResult> = flow {
        emit(MusicDirectoryListLoadResult(listOf("Test1", "Test2")))
    }
}