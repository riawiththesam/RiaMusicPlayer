package jp.riawithapps.riamusicplayer.ui.musicdirectorylist

import com.airbnb.epoxy.TypedEpoxyController
import jp.riawithapps.riamusicplayer.usecase.music.MusicFile

class AlbumListController(
    private val viewModel: AlbumListViewModel,
) : TypedEpoxyController<List<MusicFile>>() {
    override fun buildModels(data: List<MusicFile>) {
        data.forEach {
            album {
                id(it.id.rawValue)
                title(it.title)
                onClick { this@AlbumListController.viewModel.onClickItem(it) }
            }
        }
        albumListCommand {
            id("id_command")
            title("スキャン")
            onClick { this@AlbumListController.viewModel.onClickScan() }
        }
    }
}