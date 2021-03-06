package jp.riawithapps.riamusicplayer.ui.musicdirectorylist

import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import jp.riawithapps.riamusicplayer.ui.R
import jp.riawithapps.riamusicplayer.ui.databinding.EpoxyMusicDirectoryBinding

@EpoxyModelClass
abstract class AlbumModel : EpoxyModelWithHolder<AlbumModel.Holder>() {
    @EpoxyAttribute
    var title: String = ""
    @EpoxyAttribute
    var onClick: () -> Unit = {}

    override fun getDefaultLayout(): Int = R.layout.epoxy_music_directory

    override fun bind(holder: Holder) {
        holder.binding.text.text = title
        holder.binding.root.setOnClickListener { onClick() }
    }

    class Holder : EpoxyHolder() {
        lateinit var binding: EpoxyMusicDirectoryBinding

        override fun bindView(itemView: View) {
            binding = EpoxyMusicDirectoryBinding.bind(itemView)
        }
    }
}