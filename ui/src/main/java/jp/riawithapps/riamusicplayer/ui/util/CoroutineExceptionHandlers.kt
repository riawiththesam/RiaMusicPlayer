package jp.riawithapps.riamusicplayer.ui.util

import kotlinx.coroutines.CoroutineExceptionHandler

fun CoroutineExceptionHandler() = CoroutineExceptionHandler { _, e -> e.printStackTrace() }