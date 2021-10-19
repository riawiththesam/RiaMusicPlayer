package jp.riawithapps.riamusicplayer.ui.musicdirectorylist

import com.airbnb.epoxy.TypedEpoxyController

class MusicDirectoryListController: TypedEpoxyController<List<String>>() {
    override fun buildModels(data: List<String>) {
        data.forEach {
            musicDirectory {
                id(it)
                title(it)
            }
        }
    }
}