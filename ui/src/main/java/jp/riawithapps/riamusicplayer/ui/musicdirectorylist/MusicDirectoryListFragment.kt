package jp.riawithapps.riamusicplayer.ui.musicdirectorylist

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import jp.riawithapps.riamusicplayer.ui.R
import jp.riawithapps.riamusicplayer.ui.databinding.FragmentMusicDirectoryListBinding
import jp.riawithapps.riamusicplayer.ui.util.repeatCollectOnStarted
import org.koin.androidx.viewmodel.ext.android.viewModel

class MusicDirectoryListFragment : Fragment(R.layout.fragment_music_directory_list) {
    private val viewModel by viewModel<MusicDirectoryListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val controller = MusicDirectoryListController(viewModel)
        FragmentMusicDirectoryListBinding.bind(view).also { binding ->
            binding.list.setController(controller)
            controller.setData(listOf("test1", "test2"))
        }
        viewModel.directoryList.repeatCollectOnStarted(this) {
            Log.d("test", "directoryList $it")
        }
    }
}