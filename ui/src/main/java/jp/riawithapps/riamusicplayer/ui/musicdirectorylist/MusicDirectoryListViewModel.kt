package jp.riawithapps.riamusicplayer.ui.musicdirectorylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.riawithapps.riamusicplayer.usecase.music.MusicFile
import jp.riawithapps.riamusicplayer.usecase.musicdirectorylist.MusicListUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class MusicDirectoryListViewModel(
    private val musicDirectoryListUseCase: MusicListUseCase,
) : ViewModel() {
    private val handler = CoroutineExceptionHandler { _, e -> e.printStackTrace() }
    private val scope = viewModelScope + handler

    private val _directoryList = MutableStateFlow<List<MusicFile>>(emptyList())
    val directoryList: StateFlow<List<MusicFile>> = _directoryList

    private val _event = MutableSharedFlow<MusicDirectoryListEvent>()
    val event: SharedFlow<MusicDirectoryListEvent> = _event

    fun onClickScan() {
        scope.launch {
            _event.emit(MusicDirectoryListEvent.RequestPermission)
        }
    }

    fun doScan() {
        musicDirectoryListUseCase.scan()
            .onEach { _directoryList.value = it.list }
            .launchIn(scope)
    }
}

sealed class MusicDirectoryListEvent {
    object RequestPermission : MusicDirectoryListEvent()
}