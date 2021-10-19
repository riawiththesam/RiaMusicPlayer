package jp.riawithapps.riamusicplayer.usecase

interface HomeUseCase {
    fun test(): String
}

class HomeInteractor(private val repo: TestRepository): HomeUseCase {
    override fun test(): String {
        return repo.test()
    }
}
