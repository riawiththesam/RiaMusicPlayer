package jp.riawithapps.riamusicplayer

import jp.riawithapps.riamusicplayer.usecase.musicdirectorylist.MusicDirectoryListInteractor
import jp.riawithapps.riamusicplayer.usecase.musicdirectorylist.MusicListUseCase
import jp.riawithapps.riamusicplayer.usecase.player.PlayerInteractor
import jp.riawithapps.riamusicplayer.usecase.player.PlayerUseCase
import org.koin.dsl.module

fun createUseCaseModule() = module {
    single<MusicListUseCase> { MusicDirectoryListInteractor(get()) }
    single<PlayerUseCase> { PlayerInteractor() }
}