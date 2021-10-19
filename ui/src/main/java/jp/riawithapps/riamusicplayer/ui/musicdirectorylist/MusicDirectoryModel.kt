package jp.riawithapps.riamusicplayer.ui.musicdirectorylist

import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import jp.riawithapps.riamusicplayer.ui.R
import jp.riawithapps.riamusicplayer.ui.databinding.EpoxyMusicDirectoryBinding

@EpoxyModelClass
abstract class MusicDirectoryModel : EpoxyModelWithHolder<MusicDirectoryModel.Holder>() {
    @EpoxyAttribute
    var title: String = ""

    override fun getDefaultLayout(): Int = R.layout.epoxy_music_directory

    override fun bind(holder: Holder) {
        holder.binding.text.text = title
    }

    class Holder : EpoxyHolder() {
        lateinit var binding: EpoxyMusicDirectoryBinding

        override fun bindView(itemView: View) {
            binding = EpoxyMusicDirectoryBinding.bind(itemView)
        }
    }
}