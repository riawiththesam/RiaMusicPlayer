package jp.riawithapps.riamusicplayer.data

import jp.riawithapps.riamusicplayer.usecase.TestRepository

class TestRepositoryImpl: TestRepository {
    override fun test(): String {
        return "test repository impl"
    }
}