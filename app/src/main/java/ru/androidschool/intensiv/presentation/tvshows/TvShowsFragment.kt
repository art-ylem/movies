package ru.androidschool.intensiv.presentation.tvshows

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.tv_shows_fragment.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.dto.Movie
import ru.androidschool.intensiv.data.repository.TvShowRemoteRepository
import ru.androidschool.intensiv.domain.usecase.TvShowUseCase

class TvShowsFragment : Fragment(R.layout.tv_shows_fragment), TvShowPresenter.TvShowView {

    private val cd = CompositeDisposable()
    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }
    private val presenter: TvShowPresenter by lazy {
        TvShowPresenter(TvShowUseCase(TvShowRemoteRepository()))
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
        presenter.attachView(this)
        presenter.getData()
    }

    private fun intiRecyclerView() {
        tv_show_recycler_view.adapter = adapter.apply { addAll(listOf()) }
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

    override fun showMovies(movies: List<Movie>) {
        moviesLoaded(movies)
    }

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progress_bar.visibility = View.GONE
    }

    override fun showError() {
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }
}
