package ru.androidschool.intensiv.ui.movie_details

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.actor_item.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.dto.Crew
import ru.androidschool.intensiv.setUsePicasso

class ActorItem(private val item: Crew) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.actor_img_view.setUsePicasso(item.profilePath)
        viewHolder.actor_text_view.text = item.name
    }

    override fun getLayout() = R.layout.actor_item
}
