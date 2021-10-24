package jp.riawithapps.riamusicplayer.usecase.music

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class MusicId(
    val rawValue: Long,
) : Parcelable