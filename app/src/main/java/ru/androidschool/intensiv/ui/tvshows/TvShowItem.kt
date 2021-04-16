package ru.androidschool.intensiv.ui.tvshows

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.tv_show_item.*
import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.toRating

class TvShowItem(
    private val movie: Movie
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.tv_show_item_title_text_view.text = movie.tvShowTitle
        viewHolder.tv_show_item_rating.rating = movie.rating?.toRating()!!
        Picasso.get()
            .load(BuildConfig.IMAGE_URL + movie.posterPath)
            .into(viewHolder.tv_show_item_image_preview)
    }

    override fun getLayout() = R.layout.tv_show_item
}
