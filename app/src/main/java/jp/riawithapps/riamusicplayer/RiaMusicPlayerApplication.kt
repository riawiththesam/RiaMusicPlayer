package jp.riawithapps.riamusicplayer

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("UNUSED")
class RiaMusicPlayerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        initialize()
    }

    private fun initialize() {
        startKoin {
            androidContext(applicationContext)
            modules(createUseCaseModule())
            modules(createDataModule(this@RiaMusicPlayerApplication))
            modules(createViewModelModule())
        }
    }
}