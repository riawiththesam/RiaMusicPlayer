package jp.riawithapps.riamusicplayer.usecase.music

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class MusicId(
    val rawValue: Long,
) : Parcelable {
    fun getUri(): Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    fun toBundle() = Bundle().apply { putParcelable("musicId", this@MusicId) }
}

fun Bundle.toMusicId(): MusicId? {
    return this.getParcelable("musicId")
}