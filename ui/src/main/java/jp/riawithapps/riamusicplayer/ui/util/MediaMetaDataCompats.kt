package jp.riawithapps.riamusicplayer.ui.util

import android.support.v4.media.MediaMetadataCompat

fun MediaMetadataCompat.getDuration() =
    this.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)

fun MediaMetadataCompat.getTitle() =
    this.getText(MediaMetadataCompat.METADATA_KEY_TITLE)