package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.checkbox.MaterialCheckBox
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.movie_details_fragment.*
import kotlinx.android.synthetic.main.movie_param.view.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.MockRepository
import ru.androidschool.intensiv.data.MovieInfo


class MovieDetailsFragment : Fragment(R.layout.movie_details_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = MockRepository.getMovieInfo(5, R.drawable.actor)
        setUI(data)
        setToolbar()
    }

    private fun setToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(my_toolbar)
        setHasOptionsMenu(true)
        my_toolbar.title = null
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        my_toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detailed_movie_fragment_menu, menu)
        val checkBox = menu.findItem(R.id.like).actionView as MaterialCheckBox
        // При первой инициализации все равно дефолтный цвет иконки, а потом когда кликаешь - все норм. Как сделать чтобы сразу нормальный цвет отображался?
        checkBox.setButtonDrawable(R.drawable.like_selector)
        checkBox.buttonTintMode = null
    }

    private fun setUI(data: MovieInfo) {
        //set actors
        actors_recycler_view.adapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(data.actors.map { ActorItem(it) }.toList())
        }

        //set parameters
        setParams(data)

        //set other fields
        title.text = data.title
        description.text = data.description
        tv_show_item_rating.rating = data.rating

    }

    private fun setParams(data: MovieInfo) {
        data.params.forEach {
            val view = LayoutInflater.from(context).inflate(
                R.layout.movie_param, params_container,
                false
            )
            view.item_value.text = it.value
            view.item_title.text = it.title
            params_container.addView(view)
        }
    }


}
