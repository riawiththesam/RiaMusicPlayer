package jp.riawithapps.riamusicplayer.ui.player

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import jp.riawithapps.riamusicplayer.ui.R

class PlayerFragment : Fragment(R.layout.fragment_player) {
    private val args by navArgs<PlayerFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("args $args")
    }
}