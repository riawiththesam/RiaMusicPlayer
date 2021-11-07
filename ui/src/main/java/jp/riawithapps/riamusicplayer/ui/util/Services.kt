package jp.riawithapps.riamusicplayer.ui.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

fun createCoroutineScope(): CoroutineScope {
    val job = SupervisorJob()
    val exceptionHandler = kotlinx.coroutines.CoroutineExceptionHandler { _, e -> e.printStackTrace() }
    return CoroutineScope(job + exceptionHandler)
}