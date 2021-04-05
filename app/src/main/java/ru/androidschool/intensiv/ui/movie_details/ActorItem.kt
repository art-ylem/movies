package ru.androidschool.intensiv.ui.movie_details

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.actor_item.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Actor

class ActorItem(private val item: Actor) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.actor_img_view.setImageResource(item.img)
        viewHolder.actor_text_view.text = item.name
    }

    override fun getLayout() = R.layout.actor_item
}