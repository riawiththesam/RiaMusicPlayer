package jp.riawithapps.riamusicplayer

import jp.riawithapps.riamusicplayer.usecase.HomeInteractor
import jp.riawithapps.riamusicplayer.usecase.HomeUseCase
import org.koin.dsl.module

fun createUseCaseModule() = module {
    single<HomeUseCase> { HomeInteractor(get()) }
}