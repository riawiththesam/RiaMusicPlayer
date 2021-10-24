package jp.riawithapps.riamusicplayer.usecase.music

interface MusicRepository {
    suspend fun scan(): ScanResult

    class ScanResult(
        val musicList: List<MusicFile>
    )
}
