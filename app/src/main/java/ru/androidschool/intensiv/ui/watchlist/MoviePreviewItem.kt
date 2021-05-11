package ru.androidschool.intensiv.ui.watchlist

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_with_text.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.setUsePicasso

class MoviePreviewItem(
    private val content: Pair<String?, Int>,
    private val onClick: (id: Int) -> Unit
) : Item() {

    override fun getLayout() = R.layout.item_small

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.image_preview.setOnClickListener {
            onClick.invoke(content.second)
        }

        viewHolder.image_preview.setUsePicasso(content.first)
    }
}
