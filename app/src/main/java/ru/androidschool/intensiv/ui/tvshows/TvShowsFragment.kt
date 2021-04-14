package ru.androidschool.intensiv.ui.tvshows

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.tv_shows_fragment.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.myObserve
import ru.androidschool.intensiv.retrofit
import timber.log.Timber

class TvShowsFragment : Fragment(R.layout.tv_shows_fragment) {

    private val cd = CompositeDisposable()
    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        intiRecyclerView()
        getMovies()
    }

    private fun hideProgressBar() {
        progress_bar.visibility = View.GONE
    }

    private fun showProgressBar() {
        progress_bar.visibility = View.VISIBLE
    }

    private fun intiRecyclerView() {
        tv_show_recycler_view.adapter = adapter.apply { addAll(listOf()) }
    }

    private fun getMovies() {
        val dis = retrofit.tvShowRequest()
            .myObserve()
            .doOnSubscribe{showProgressBar()}
            .doFinally { hideProgressBar() }
            .subscribe({ moviesLoaded(it.results) }, { Timber.e(it) })

        cd.add(dis)
    }

    private fun moviesLoaded(results: List<Movie>?) {
        results?.let { list ->
            val tvShows = list.map { movie -> TvShowItem(movie) }.toList()
            tv_show_recycler_view.adapter = adapter.apply { addAll(tvShows) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cd.clear()
    }
}
