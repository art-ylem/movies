package ru.androidschool.intensiv.ui.feed

import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import ru.androidschool.intensiv.MainActivity
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.data.MovieResponse
import ru.androidschool.intensiv.myObserve
import ru.androidschool.intensiv.retrofit
import timber.log.Timber

class FeedFragment : Fragment(R.layout.feed_fragment) {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }
    private val cd = CompositeDisposable()

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
        zipObservables()
        searchBarSetting()
    }

    private fun searchBarSetting() {
        val dis = search_toolbar.changeListener()
            .subscribe({ Timber.d(it) }, { Timber.e(it) })
        cd.add(dis)
    }

    private fun zipObservables() {
        val dis = Single.zip(
            // QUESTION:  хочу чтобы при ошибке одного запроса все остальные запросы не страдали.
            // Так норм сделать? Если будет у какого-то запроса error, то просто вернет null, а потом я ответ проверю на null
            retrofit.popularMoviesRequest().onErrorResumeNext { Single.just(null) },
            retrofit.upcomingMoviesRequest().onErrorResumeNext { Single.just(null) },
            retrofit.nowPlayingMoviesRequest().onErrorResumeNext { Single.just(null) },
            Function3<MovieResponse?, MovieResponse?, MovieResponse?,
                    HashMap<BlockMovies, MovieResponse>> { response1, response2, response3 ->
                hashMapOf(
                    BlockMovies.UPCOMING to response1,
                    BlockMovies.POPULAR to response2,
                    BlockMovies.NOW_PLAYING to response3
                )
            }).myObserve()
            .doOnSubscribe { showProgressBar() }
            .doFinally { hideProgressBar() }
            .subscribe({ moviesLoaded(it) }, { err -> Timber.e(err) })
        cd.add(dis)
    }

    private fun hideProgressBar() {
        progress_bar.visibility = View.GONE
    }

    private fun showProgressBar() {
        progress_bar.visibility = View.VISIBLE
    }

    private fun moviesLoaded(results: HashMap<BlockMovies, MovieResponse>) {
        results.keys.forEach {
            when (it) {
                BlockMovies.NOW_PLAYING -> results[it]?.results?.let { movies ->
                    addBlockToAdapter(
                        movies,
                        R.string.nowPlaying
                    )
                }
                BlockMovies.POPULAR -> results[it]?.results?.let { movies ->
                    addBlockToAdapter(
                        movies,
                        R.string.popular
                    )
                }
                BlockMovies.UPCOMING -> results[it]?.results?.let { movies ->
                    addBlockToAdapter(
                        movies,
                        R.string.upcoming
                    )
                }
            }
        }
    }

    private fun addBlockToAdapter(results: List<Movie>, @StringRes title: Int) {
        results?.let { list ->
            val newMoviesList = listOf(
                MainCardContainer(
                    title,
                    list.map { movie ->
                        MovieItem(movie) { movie ->
                            openMovieDetails(movie)
                        }
                    }.toList()
                )
            )
            movies_recycler_view.adapter = adapter.apply { addAll(newMoviesList) }
        }
    }

    private fun openMovieDetails(movie: Movie) {
        val bundle = Bundle()
        bundle.putString(KEY_TITLE, movie.title)
        bundle.putString(movieId, movie.id.toString())
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

    private fun openSearch(searchText: String) {
        val bundle = Bundle()
        bundle.putString(KEY_SEARCH, searchText)
        findNavController().navigate(R.id.search_dest, bundle, options)
    }

    override fun onStop() {
        super.onStop()
        search_toolbar.clear()
        cd.clear()
        adapter.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    companion object {
        const val MIN_LENGTH = 3
        const val KEY_TITLE = "title"
        const val KEY_SEARCH = "search"
        private val TAG = MainActivity::class.java.simpleName
        private const val movieId = "movieId"
    }

    enum class BlockMovies {
        POPULAR, NOW_PLAYING, UPCOMING
    }
}
