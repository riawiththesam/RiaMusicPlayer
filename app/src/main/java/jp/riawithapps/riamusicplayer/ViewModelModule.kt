package jp.riawithapps.riamusicplayer

import jp.riawithapps.riamusicplayer.ui.musicdirectorylist.MusicDirectoryListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun createViewModelModule() = module {
    viewModel { MusicDirectoryListViewModel(get()) }
}