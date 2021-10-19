package jp.riawithapps.riamusicplayer.ui.musicdirectorylist

import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import jp.riawithapps.riamusicplayer.ui.R
import jp.riawithapps.riamusicplayer.ui.databinding.EpoxyMusicDirectoryCommandBinding

@EpoxyModelClass
abstract class MusicDirectoryCommandModel
    : EpoxyModelWithHolder<MusicDirectoryCommandModel.Holder>() {
    @EpoxyAttribute
    var title: String = ""

    @EpoxyAttribute
    var onClick: () -> Unit = {}

    override fun getDefaultLayout(): Int = R.layout.epoxy_music_directory_command

    override fun bind(holder: Holder) {
        holder.binding.text.text = title
        holder.binding.root.setOnClickListener { onClick() }
    }

    class Holder : EpoxyHolder() {
        lateinit var binding: EpoxyMusicDirectoryCommandBinding

        override fun bindView(itemView: View) {
            binding = EpoxyMusicDirectoryCommandBinding.bind(itemView)
        }
    }
}