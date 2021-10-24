package jp.riawithapps.riamusicplayer.ui.musicdirectorylist

import androidx.lifecycle.ViewModel
import jp.riawithapps.riamusicplayer.ui.util.createScope
import jp.riawithapps.riamusicplayer.ui.util.emit
import jp.riawithapps.riamusicplayer.usecase.music.MusicFile
import jp.riawithapps.riamusicplayer.usecase.musicdirectorylist.MusicListUseCase
import kotlinx.coroutines.flow.*

class AlbumListViewModel(
    private val musicListUseCase: MusicListUseCase,
) : ViewModel() {
    private val scope = createScope()

    private val _musicList = MutableStateFlow<List<MusicFile>>(emptyList())
    val musicList: StateFlow<List<MusicFile>> = _musicList

    private val _event = MutableSharedFlow<AlbumListEvent>()
    val event: SharedFlow<AlbumListEvent> = _event

    fun onClickItem() {
        _event.emit(scope, AlbumListEvent.NavigateToPlayer)
    }

    fun onClickScan() {
        _event.emit(scope, AlbumListEvent.RequestPermission)
    }

    fun doScan() {
        musicListUseCase.scan()
            .onEach { _musicList.value = it.list }
            .launchIn(scope)
    }
}

sealed class AlbumListEvent {
    object RequestPermission : AlbumListEvent()
    object NavigateToPlayer : AlbumListEvent()
}