package jp.riawithapps.riamusicplayer.usecase.music

data class MusicFile(
    val id: MusicId,
    val title: String,
    val albumTitle: String,
    val artist: String,
)