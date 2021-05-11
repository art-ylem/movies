package ru.androidschool.intensiv.presentation.feed

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_with_text.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.dto.Movie
import ru.androidschool.intensiv.setUsePicasso
import ru.androidschool.intensiv.toRating

class MovieItem(
    private val content: Movie,
    private val onClick: (movie: Movie) -> Unit
) : Item() {

    override fun getLayout() = R.layout.item_with_text

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.description.text = content.title
        viewHolder.movie_rating.rating = content.rating?.toRating()!!
        viewHolder.content.setOnClickListener {
            onClick.invoke(content)
        }
        viewHolder.image_preview.setUsePicasso(content.posterPath)
    }
}
