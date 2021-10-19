package jp.riawithapps.riamusicplayer

import jp.riawithapps.riamusicplayer.ui.MainViewModel
import jp.riawithapps.riamusicplayer.ui.musicdirectorylist.MusicDirectoryListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun createViewModelModule() = module {
    viewModel { MainViewModel(get()) }
    viewModel { MusicDirectoryListViewModel() }
}