package jp.riawithapps.riamusicplayer.usecase

interface HomeUseCase {
    fun test(): String
}

class HomeInteractor: HomeUseCase {
    override fun test(): String {
        return "test"
    }
}
