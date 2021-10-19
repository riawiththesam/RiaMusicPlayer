package jp.riawithapps.riamusicplayer.ui

import androidx.lifecycle.ViewModel
import jp.riawithapps.riamusicplayer.usecase.HomeUseCase

class MainViewModel(
    private val homeUseCase: HomeUseCase,
): ViewModel() {
    fun test() = homeUseCase.test()
}