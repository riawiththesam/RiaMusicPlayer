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
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment(R.layout.fragment_player) {
    private val args by navArgs<PlayerFragmentArgs>()
    private val playerViewModel by viewModel<PlayerViewModel>()

    private val mediaBrowser: MediaBrowserCompat by lazy {
        MediaBrowserCompat(
            requireContext(),
            ComponentName(requireContext(), MusicPlayerService::class.java),
            connectionCallbacks,
            null,
        )
    }

    private val connectionCallbacks = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            MediaControllerCompat.setMediaController(
                requireActivity(),
                MediaControllerCompat(context, mediaBrowser.sessionToken),
            )

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

        FragmentPlayerBinding.bind(view)
            .bind(this, playerViewModel)

        playerViewModel.event.repeatCollectOnStarted(this) {
            when (it) {
                is PlayerEvent.Seek -> requestSeek(it)
                is PlayerEvent.Pause -> requestPause()
                is PlayerEvent.Play -> requestPlay()
            }
        }

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

    private fun requestSeek(event: PlayerEvent.Seek) {
        MediaControllerCompat.getMediaController(requireActivity())?.transportControls?.seekTo(event.to.toMillis())
    }

    private fun requestPause() {
        MediaControllerCompat.getMediaController(requireActivity())?.transportControls?.pause()
    }

    private fun requestPlay() {
        MediaControllerCompat.getMediaController(requireActivity())?.transportControls?.play()
    }
}
