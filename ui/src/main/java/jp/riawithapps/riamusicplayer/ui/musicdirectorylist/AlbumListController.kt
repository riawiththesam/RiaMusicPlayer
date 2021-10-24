package jp.riawithapps.riamusicplayer.ui.musicdirectorylist

import com.airbnb.epoxy.TypedEpoxyController

class AlbumListController(
    private val viewModel: MusicDirectoryListViewModel,
) : TypedEpoxyController<List<String>>() {
    override fun buildModels(data: List<String>) {
        data.forEach {
            album {
                id(it)
                title(it)
            }
        }
        albumListCommand {
            id("id_command")
            title("スキャン")
            onClick { this@AlbumListController.viewModel.onClickScan() }
        }
    }
}