package jp.riawithapps.riamusicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import jp.riawithapps.riamusicplayer.data.TestRepositoryImpl
import jp.riawithapps.riamusicplayer.usecase.HomeInteractor

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("test", HomeInteractor(TestRepositoryImpl()).test())
    }
}