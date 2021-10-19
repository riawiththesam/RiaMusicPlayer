package jp.riawithapps.riamusicplayer

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("UNUSED")
class RiaMusicPlayerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        startKoin {
            androidContext(applicationContext)
            modules(createUseCaseModule())
            modules(createDataModule())
            modules(createViewModelModule())
        }
    }
}