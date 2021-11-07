package jp.riawithapps.riamusicplayer.usecase.util

import kotlinx.coroutines.flow.flow

/**
 * 自動でUnitをemitするflow
 *
 * @param block
 * @return
 */
fun singleUnitFlow(block: suspend () -> Unit) = flow {
    block()
    emit(Unit)
}