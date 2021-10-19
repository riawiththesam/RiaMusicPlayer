package jp.riawithapps.riamusicplayer

import jp.riawithapps.riamusicplayer.data.TestRepositoryImpl
import jp.riawithapps.riamusicplayer.usecase.TestRepository
import org.koin.dsl.module

fun createDataModule() = module {
    single<TestRepository> { TestRepositoryImpl() }
}