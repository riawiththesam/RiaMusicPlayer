package jp.riawithapps.riamusicplayer.ui.musicdirectorylist

import com.airbnb.epoxy.TypedEpoxyController

class MusicDirectoryListController(
    private val viewModel: MusicDirectoryListViewModel,
) : TypedEpoxyController<List<String>>() {
    override fun buildModels(data: List<String>) {
        data.forEach {
            musicDirectory {
                id(it)
                title(it)
            }
        }
        musicDirectoryCommand {
            id("id_command")
            title("スキャン")
            onClick { this@MusicDirectoryListController.viewModel.onClickScan() }
        }
    }
}