package ru.androidschool.intensiv.ui.feed

import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import ru.androidschool.intensiv.*
import ru.androidschool.intensiv.data.Movie
import timber.log.Timber

class FeedFragment : Fragment() {

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.feed_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        searchBarSetting()
    }

    private fun searchBarSetting() {
        val dis = search_toolbar.changeListener()
            .subscribe({ Timber.d(it) }, { Timber.e(it) })
        cd.add(dis)
    }

    private fun loadData() {
        val dis = retrofit.upcomingMoviesRequest(API_KEY)
            .myObserve()
            .subscribe({ moviesLoaded(it.results, R.string.upcoming) }, { err -> Timber.e(err) })
        cd.add(dis)

        val dis2 = retrofit.popularMoviesRequest(API_KEY)
            .myObserve()
            .subscribe({ moviesLoaded(it.results, R.string.popular) }, { err -> Timber.e(err) })
        cd.add(dis2)
    }

    private fun moviesLoaded(results: List<Movie>?, @StringRes title: Int) {
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
        private const val API_KEY = BuildConfig.THE_MOVIE_DATABASE_API
        private const val movieId = "movieId"
    }
}

