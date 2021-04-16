package ru.androidschool.intensiv.ui.movie_details

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.actor_item.*
import kotlinx.android.synthetic.main.tv_show_item.*
import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Crew

class ActorItem(private val item: Crew) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        Picasso.get()
            .load(BuildConfig.IMAGE_URL + item.profilePath)
            .into(viewHolder.actor_img_view)
        viewHolder.actor_text_view.text = item.name
    }

    override fun getLayout() = R.layout.actor_item
}
