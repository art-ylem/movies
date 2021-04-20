package ru.androidschool.intensiv.ui.watchlist

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_with_text.*
import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.R

class MoviePreviewItem(
    private val content: Pair<String?, Int>,
    private val onClick: (id: Int) -> Unit
) : Item() {

    override fun getLayout() = R.layout.item_small

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.image_preview.setOnClickListener {
            onClick.invoke(content.second)
        }

        Picasso.get()
            .load(BuildConfig.IMAGE_URL + content.first)
            .into(viewHolder.image_preview)
    }
}
