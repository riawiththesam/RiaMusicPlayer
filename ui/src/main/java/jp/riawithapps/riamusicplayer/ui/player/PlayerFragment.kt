package jp.riawithapps.riamusicplayer.ui.player

import android.content.ComponentName
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
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

    lateinit var mediaBrowser: MediaBrowserCompat

    private val connectionCallbacks = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            val mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken)
            mediaController.registerCallback(controllerCallback)

            MediaControllerCompat.setMediaController(
                requireActivity(),
                mediaController,
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
                null
            )
        }
    }

    private val controllerCallback = object : MediaControllerCompat.Callback() {
        // 追加
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            when (state?.state) {
                PlaybackStateCompat.STATE_PLAYING -> {
                }
                else -> {
                }
            }
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            if (metadata != null) playerViewModel.onMetaDataChanged(metadata)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentPlayerBinding.bind(view)
        playerViewModel.metaData.repeatCollectOnStarted(this) { meta ->
            binding.infoTitle.text = meta.title
            binding.seekBarDuration.text = meta.duration
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
                MediaControllerCompat.getMediaController(requireActivity())?.unregisterCallback(controllerCallback)
                mediaBrowser.disconnect()
            }
        })
    }
}