package ru.androidschool.intensiv.presentation.tvshows

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.tv_show_item.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.dto.Movie
import ru.androidschool.intensiv.setUsePicasso
import ru.androidschool.intensiv.toRating

class TvShowItem(
    private val movie: Movie
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.tv_show_item_title_text_view.text = movie.tvShowTitle
        viewHolder.tv_show_item_rating.rating = movie.rating?.toRating()!!

        viewHolder.tv_show_item_image_preview.setUsePicasso(movie.posterPath)
    }

    override fun getLayout() = R.layout.tv_show_item
}
