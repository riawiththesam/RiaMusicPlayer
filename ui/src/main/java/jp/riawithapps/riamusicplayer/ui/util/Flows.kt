package jp.riawithapps.riamusicplayer.ui.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * FragmentでFlowをcollectする
 * Fragmentの場合、viewLifecycleOwnerを利用する必要があるので、この拡張を利用する
 */
inline fun <T> Flow<T>.repeatCollectOnStarted(
    fragment: Fragment,
    crossinline action: suspend (value: T) -> Unit,
) {
    fragment.viewLifecycleOwner.lifecycleScope.launch {
        fragment.viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            collect { action(it) }
        }
    }
}