package jp.riawithapps.riamusicplayer.ui.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

fun <T> MutableStateFlow<T>.emit(scope: CoroutineScope, value: T) {
    scope.launch { emit(value) }
}