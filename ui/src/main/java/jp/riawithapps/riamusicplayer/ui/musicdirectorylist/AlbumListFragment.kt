package jp.riawithapps.riamusicplayer.ui.musicdirectorylist

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import jp.riawithapps.riamusicplayer.ui.R
import jp.riawithapps.riamusicplayer.ui.databinding.FragmentMusicDirectoryListBinding
import jp.riawithapps.riamusicplayer.ui.root.RootViewModel
import jp.riawithapps.riamusicplayer.ui.util.repeatCollectOnStarted
import jp.riawithapps.riamusicplayer.usecase.music.MusicId
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumListFragment : Fragment(R.layout.fragment_music_directory_list) {
    private val viewModel by viewModel<AlbumListViewModel>()
    private val rootViewModel by sharedViewModel<RootViewModel>()

    private val permissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            viewModel.doScan()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val controller = AlbumListController(viewModel)
        FragmentMusicDirectoryListBinding.bind(view).also { binding ->
            binding.list.setController(controller)
        }
        viewModel.musicList.repeatCollectOnStarted(this) { list ->
            controller.setData(list)
        }
        viewModel.event.repeatCollectOnStarted(this) { event ->
            when (event) {
                is AlbumListEvent.RequestPermission -> requestPermissions()
                is AlbumListEvent.NavigateToPlayer -> navigateToPlayer(event.music.id)
            }
        }
    }

    private fun requestPermissions() {
        if (context?.hasReadPermission() == true) {
            viewModel.doScan()
        } else {
            permissionRequest.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun navigateToPlayer(id: MusicId) {
        rootViewModel.navigateToPlayer(id)
    }

    private fun Context.hasReadPermission(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }
}