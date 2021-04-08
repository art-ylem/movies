package ru.androidschool.intensiv.ui.movie_details

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.actor_item.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Crew

class ActorItem(private val item: Crew) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        // что-то не смог картинку получить с обрезанного урла
//        viewHolder.actor_img_view.setImageResource(item.img)
        viewHolder.actor_text_view.text = item.name
    }

    override fun getLayout() = R.layout.actor_item
}
