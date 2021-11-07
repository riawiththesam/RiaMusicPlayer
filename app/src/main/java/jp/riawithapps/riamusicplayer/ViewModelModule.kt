package jp.riawithapps.riamusicplayer

import jp.riawithapps.riamusicplayer.ui.musicdirectorylist.AlbumListViewModel
import jp.riawithapps.riamusicplayer.ui.player.PlayerViewModel
import jp.riawithapps.riamusicplayer.ui.root.RootViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun createViewModelModule() = module {
    viewModel { RootViewModel() }
    viewModel { AlbumListViewModel(get()) }
    viewModel { PlayerViewModel(get()) }
}