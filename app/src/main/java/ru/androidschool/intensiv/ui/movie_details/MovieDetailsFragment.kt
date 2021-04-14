package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.movie_details_fragment.*
import kotlinx.android.synthetic.main.movie_param.view.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.DetailedMovie
import ru.androidschool.intensiv.data.MovieCredits
import ru.androidschool.intensiv.data.MovieInfo
import ru.androidschool.intensiv.myObserve
import ru.androidschool.intensiv.retrofit
import ru.androidschool.intensiv.toRating
import timber.log.Timber

class MovieDetailsFragment : Fragment(R.layout.movie_details_fragment) {

    private val cd = CompositeDisposable()
    private val adapter by lazy {
        GroupAdapter<com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder>()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieId = requireArguments().getString(movieId)
        movieId?.let { loadData(it) }
        setToolbar()
    }

    private fun loadData(id: String) {
        //QUESTION: у Single'a можно просто в onError скрывать progressBar?
        val dis = Single.zip(
            retrofit.movieCreditsByIdRequest(id),
            retrofit.movieInfoByIdRequest(id),
            BiFunction<MovieCredits?, MovieInfo?, DetailedMovie?> { movieCredits, movieInfo ->
                DetailedMovie(
                    movieCredits,
                    movieInfo
                )
            })
            .doOnSubscribe { showProgressBar() }
            .myObserve()
            .doOnSuccess { hideProgressBar() }
            .subscribe({
                dataLoaded(it) }, {
                Timber.e(it) })

        cd.add(dis)
    }

    private fun dataLoaded(detailedMovie: DetailedMovie?) {
        detailedMovie?.credits?.let { setCredits(it) }
        detailedMovie?.info?.let { setInfo(it) }
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
        val checkBox = menu.findItem(R.id.like).actionView as CheckBox
        checkBox.setButtonDrawable(R.drawable.like_selector)
        checkBox.setPadding(0, 0, 32, 0)
        checkBox.buttonTintMode = null
    }

    private fun setInfo(data: MovieInfo) {
        // set parameters
        setParams(data)

        // set other fields
        title.text = data.title
        description.text = data.overview
        tv_show_item_rating.rating = data.voteAverage?.toRating()!!
    }

    private fun hideProgressBar() {
        //QUESTION: что-то не пойму как progress_bar расположить по середине экрана, nested scroll view не позволяет так делать?
        progress_bar.visibility = View.GONE
        main_container.visibility = View.VISIBLE
    }

    private fun showProgressBar() {
        progress_bar.visibility = View.VISIBLE
        main_container.visibility = View.GONE
    }

    private fun setCredits(data: MovieCredits) {
        // set actors
        actors_recycler_view.adapter = adapter.apply {
            data.crew?.map { ActorItem(it) }?.toList()?.let { addAll(it) }
        }
    }

    private fun setParams(data: MovieInfo) {
        // add year
        data.releaseDate?.let {
            if (it.length > yearSubstring) {
                val view = LayoutInflater.from(context).inflate(
                    R.layout.movie_param, params_container,
                    false
                )

                view.item_value.text = it.subSequence(0, 4)
                view.item_title.text = requireContext().getString(R.string.year)
                params_container.addView(view)
            }
        }

        // add genres
        data.genres?.let { list ->
            val view = LayoutInflater.from(context).inflate(
                R.layout.movie_param, params_container,
                false
            )
            val value = ArrayList<String>()
            list.forEach { it.name?.let { name -> value.add(name) } }
            view.item_value.text = value.joinToString()
            view.item_title.text = requireContext().getString(R.string.genres)
            params_container.addView(view)
        }
    }

    override fun onStop() {
        super.onStop()
        adapter.clear()
        cd.clear()
    }

    companion object {
        // QUESTION: все "magic number" вынести в companion object?
        private const val yearSubstring = 4
        private const val movieId = "movieId"
    }
}
