package jp.riawithapps.riamusicplayer.ui.player

import android.content.ComponentName
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.navArgs
import jp.riawithapps.riamusicplayer.ui.R
import jp.riawithapps.riamusicplayer.ui.databinding.FragmentPlayerBinding
import jp.riawithapps.riamusicplayer.ui.service.MusicPlayerService
import jp.riawithapps.riamusicplayer.ui.util.repeatCollectOnStarted
import jp.riawithapps.riamusicplayer.usecase.player.PlayerData
import jp.riawithapps.riamusicplayer.usecase.player.PlayerMetaData
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment(R.layout.fragment_player) {
    private val args by navArgs<PlayerFragmentArgs>()
    private val playerViewModel by viewModel<PlayerViewModel>()

    lateinit var mediaBrowser: MediaBrowserCompat

    private val connectionCallbacks = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            MediaControllerCompat.setMediaController(
                requireActivity(),
                MediaControllerCompat(context, mediaBrowser.sessionToken),
            )

            // 接続したので、曲リストを購読します。ここでparentIdを渡しています。
            mediaBrowser.subscribe(mediaBrowser.root, subscriptionCallback)
        }
    }

    private val subscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(
            parentId: String,
            children: MutableList<MediaBrowserCompat.MediaItem>
        ) {
            MediaControllerCompat.getMediaController(requireActivity())?.transportControls?.playFromUri(
                args.id.getUri(),
                args.id.toBundle(),
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentPlayerBinding.bind(view)
        playerViewModel.metaData.repeatCollectOnStarted(this) { metaData ->
            binding.infoTitle.text = metaData.title
            binding.seekBarDuration.text = metaData.getDurationText()
        }
        playerViewModel.playerData.repeatCollectOnStarted(this) { playerData ->
            binding.seekBarCurrentTime.text = playerData.getCurrentTimeText()
        }

        mediaBrowser = MediaBrowserCompat(
            requireContext(),
            ComponentName(requireContext(), MusicPlayerService::class.java),
            connectionCallbacks,
            null // optional Bundle
        )

        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                mediaBrowser.connect()
            }

            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                mediaBrowser.disconnect()
            }
        })
    }
}

private fun PlayerMetaData.getDurationText() = this.duration.toString()

private fun PlayerData.getCurrentTimeText() = this.currentTime.toString()
