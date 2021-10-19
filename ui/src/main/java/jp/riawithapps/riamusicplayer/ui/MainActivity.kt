package jp.riawithapps.riamusicplayer.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import jp.riawithapps.riamusicplayer.usecase.HomeUseCase
import org.koin.android.ext.android.get

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val usecase = get<HomeUseCase>()
        Log.d("test", usecase.test())
    }
}
