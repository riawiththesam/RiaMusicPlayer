package jp.riawithapps.riamusicplayer.ui.musicdirectorylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.riawithapps.riamusicplayer.usecase.musicdirectorylist.MusicDirectoryListUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus

class MusicDirectoryListViewModel(
    private val musicDirectoryListUseCase: MusicDirectoryListUseCase,
) : ViewModel() {
    private val handler = CoroutineExceptionHandler { _, e -> e.printStackTrace() }
    private val scope = viewModelScope + handler

    private val _directoryList = MutableStateFlow<List<String>>(emptyList())
    val directoryList: StateFlow<List<String>> = _directoryList

    fun onClickScan() {
        musicDirectoryListUseCase.scan()
            .onEach { _directoryList.value = it.list }
            .launchIn(scope)
    }
}