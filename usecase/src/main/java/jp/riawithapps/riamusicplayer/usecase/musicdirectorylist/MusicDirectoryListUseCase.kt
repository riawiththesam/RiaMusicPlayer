package jp.riawithapps.riamusicplayer.usecase.musicdirectorylist

interface MusicDirectoryListUseCase {
    fun load(): MusicDirectoryListLoadResult
}

class MusicDirectoryListLoadResult

class MusicDirectoryListInteractor: MusicDirectoryListUseCase {
    override fun load(): MusicDirectoryListLoadResult {
        return MusicDirectoryListLoadResult()
    }
}