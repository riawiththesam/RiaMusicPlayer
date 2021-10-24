package jp.riawithapps.riamusicplayer.ui.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.riawithapps.riamusicplayer.ui.util.emit
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.plus

class RootViewModel : ViewModel() {
    private val handler = CoroutineExceptionHandler { _, e -> e.printStackTrace() }
    private val scope = viewModelScope + handler

    private val _event = MutableSharedFlow<RootEvent>()
    val event: SharedFlow<RootEvent> = _event

    fun navigateToPlayer() {
        _event.emit(scope, RootEvent.NavigateToPlayer)
    }
}

sealed class RootEvent {
    object NavigateToPlayer : RootEvent()
}