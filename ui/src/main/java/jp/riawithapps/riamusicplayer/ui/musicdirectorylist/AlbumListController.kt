package jp.riawithapps.riamusicplayer.ui.musicdirectorylist

import com.airbnb.epoxy.TypedEpoxyController

class AlbumListController(
    private val viewModel: AlbumListViewModel,
) : TypedEpoxyController<List<String>>() {
    override fun buildModels(data: List<String>) {
        data.forEach {
            album {
                id(it)
                title(it)
                onClick { this@AlbumListController.viewModel.onClickItem() }
            }
        }
        albumListCommand {
            id("id_command")
            title("スキャン")
            onClick { this@AlbumListController.viewModel.onClickScan() }
        }
    }
}