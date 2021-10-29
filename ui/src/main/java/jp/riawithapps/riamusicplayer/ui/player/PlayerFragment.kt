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
import jp.riawithapps.riamusicplayer.ui.service.MusicPlayerService

class PlayerFragment : Fragment(R.layout.fragment_player) {
    private val args by navArgs<PlayerFragmentArgs>()

    lateinit var mediaBrowser: MediaBrowserCompat

    private val subscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(
            parentId: String,
            children: MutableList<MediaBrowserCompat.MediaItem>
        ) {
            // 曲リストを受け取ってますが、1曲だけなので、特に利用してません。とにかくprepareを呼び出します。
            MediaControllerCompat.getMediaController(requireActivity())?.transportControls?.playFromUri(
                args.id.getUri(),
                null
            )
        }
    }

    private val connectionCallbacks = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            // 接続後、受け取ったTokenで操作するようにします。
            MediaControllerCompat.setMediaController(
                requireActivity(),
                MediaControllerCompat(context, mediaBrowser.sessionToken)
            )

            // 接続したので、曲リストを購読します。ここでparentIdを渡しています。
            mediaBrowser.subscribe(mediaBrowser.root, subscriptionCallback)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("args $args")

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

        MediaControllerCompat.getMediaController(requireActivity())?.transportControls?.playFromMediaId(
            args.id.rawValue.toString(),
            null
        )
    }
}