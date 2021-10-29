package jp.riawithapps.riamusicplayer.usecase.music

import android.content.ContentUris
import android.os.Parcelable
import android.provider.MediaStore
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class MusicId(
    val rawValue: Long,
) : Parcelable {

    fun getUri() = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, rawValue)
}