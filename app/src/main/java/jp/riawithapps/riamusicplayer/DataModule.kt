package jp.riawithapps.riamusicplayer

import android.app.Application
import jp.riawithapps.riamusicplayer.data.TestRepositoryImpl
import jp.riawithapps.riamusicplayer.data.musiclist.MusicRepositoryImpl
import jp.riawithapps.riamusicplayer.usecase.TestRepository
import jp.riawithapps.riamusicplayer.usecase.music.MusicRepository
import org.koin.dsl.module

fun createDataModule(application: Application) = module {
    single<TestRepository> { TestRepositoryImpl() }
    single<MusicRepository> { MusicRepositoryImpl(application.contentResolver) }
}