package jp.riawithapps.riamusicplayer.data.musiclist

import android.content.ContentResolver
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import jp.riawithapps.riamusicplayer.usecase.music.MusicFile
import jp.riawithapps.riamusicplayer.usecase.music.MusicId
import jp.riawithapps.riamusicplayer.usecase.music.MusicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MusicRepositoryImpl(
    private val contentResolver: ContentResolver,
) : MusicRepository {
    override suspend fun scan(): MusicRepository.ScanResult {
        return withContext(Dispatchers.IO) {
            val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val projection = listOf(
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.ArtistColumns.ARTIST,
                MediaStore.Audio.Media._ID
            ).toTypedArray()
            val selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?"
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3")
            val selectionArgsMp3 = listOf(mimeType)
            val list = mutableListOf<MusicFile>()
            contentResolver.query(
                uri,
                projection,
                selectionMimeType,
                selectionArgsMp3.toTypedArray(),
                null
            )?.use {
                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val albumColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.ArtistColumns.ARTIST)
                val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                while (it.moveToNext()) {
                    val title = it.getString(nameColumn)
                    val album = it.getString(albumColumn)
                    val albumId = it.getLong(albumIdColumn)
                    val artist = it.getString(artistColumn)
                    val id = it.getLong(idColumn)
                    list.add(MusicFile(MusicId(id), title, album, artist))
                }
            }
            MusicRepository.ScanResult(
                musicList = list
            )
        }
    }
}
