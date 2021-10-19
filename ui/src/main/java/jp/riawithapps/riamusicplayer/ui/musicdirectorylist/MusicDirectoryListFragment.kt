package jp.riawithapps.riamusicplayer.ui.musicdirectorylist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import jp.riawithapps.riamusicplayer.ui.R
import jp.riawithapps.riamusicplayer.ui.databinding.FragmentMusicDirectoryListBinding

class MusicDirectoryListFragment : Fragment(R.layout.fragment_music_directory_list) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FragmentMusicDirectoryListBinding.bind(view).also { binding ->
            val controller = MusicDirectoryListController()
            binding.list.setController(controller)
            controller.setData(listOf("test1", "test2"))
        }
    }
}