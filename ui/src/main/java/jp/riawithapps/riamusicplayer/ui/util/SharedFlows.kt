package jp.riawithapps.riamusicplayer.ui.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

fun <T> MutableSharedFlow<T>.emit(scope: CoroutineScope, value: T) {
    scope.launch { emit(value) }
}