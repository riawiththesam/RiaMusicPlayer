package jp.riawithapps.riamusicplayer.ui.root

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import jp.riawithapps.riamusicplayer.ui.R
import jp.riawithapps.riamusicplayer.ui.databinding.FragmentRootBinding
import jp.riawithapps.riamusicplayer.ui.musicselect.MusicSelectFragment
import jp.riawithapps.riamusicplayer.ui.search.SearchFragment
import jp.riawithapps.riamusicplayer.ui.util.repeatCollectOnStarted
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RootFragment : Fragment(R.layout.fragment_root) {
    private val rootViewModel by sharedViewModel<RootViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = listOf(
            MusicSelectFragment(),
            SearchFragment(),
        )
        val adapter = HomeViewPagerAdapter(list, childFragmentManager, lifecycle)

        FragmentRootBinding.bind(view).also { binding ->
            binding.viewpager.adapter = adapter
            binding.viewpager.isUserInputEnabled = false

            binding.navigation.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.action_select -> binding.viewpager.setCurrentItem(0, false)
                    R.id.action_search -> binding.viewpager.setCurrentItem(1, false)
                    R.id.action_library -> binding.viewpager.setCurrentItem(0, false)
                    R.id.action_settings -> binding.viewpager.setCurrentItem(1, false)
                }
                true
            }
        }

        rootViewModel.event.repeatCollectOnStarted(this) { event ->
            when (event) {
                is RootEvent.NavigateToPlayer -> {
                    findNavController().navigate(RootFragmentDirections.actionToPlayer(event.id))
                }
            }
        }
    }
}

private class HomeViewPagerAdapter(
    private val fragmentList: List<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}
