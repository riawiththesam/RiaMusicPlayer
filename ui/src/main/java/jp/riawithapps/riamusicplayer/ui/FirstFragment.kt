package jp.riawithapps.riamusicplayer.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import jp.riawithapps.riamusicplayer.ui.databinding.FragmentFirstBinding
import jp.riawithapps.riamusicplayer.ui.musicfilelist.MusicFileListFragment
import jp.riawithapps.riamusicplayer.ui.search.SearchFragment

class FirstFragment : Fragment(R.layout.fragment_first) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = listOf(
            MusicFileListFragment(),
            SearchFragment(),
        )
        val adapter = HomeViewPagerAdapter(list, childFragmentManager, lifecycle)

        FragmentFirstBinding.bind(view).also {
            it.viewpager.adapter = adapter
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
