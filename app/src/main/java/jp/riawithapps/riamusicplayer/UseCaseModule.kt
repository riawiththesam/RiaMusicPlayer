package jp.riawithapps.riamusicplayer

import jp.riawithapps.riamusicplayer.usecase.HomeInteractor
import jp.riawithapps.riamusicplayer.usecase.HomeUseCase
import jp.riawithapps.riamusicplayer.usecase.musicdirectorylist.MusicDirectoryListInteractor
import jp.riawithapps.riamusicplayer.usecase.musicdirectorylist.MusicListUseCase
import org.koin.dsl.module

fun createUseCaseModule() = module {
    single<HomeUseCase> { HomeInteractor(get()) }
    single<MusicListUseCase> { MusicDirectoryListInteractor(get()) }
}