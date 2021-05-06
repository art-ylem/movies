package ru.androidschool.intensiv.presentation.feed

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import ru.androidschool.intensiv.*
import ru.androidschool.intensiv.data.dto.Movie
import ru.androidschool.intensiv.data.dto.MovieResponse
import ru.androidschool.intensiv.data.repository.FeedRemoteRepository
import ru.androidschool.intensiv.domain.usecase.FeedUseCase
import timber.log.Timber
import javax.inject.Inject

class FeedFragment : BaseStateFragment(R.layout.feed_fragment) {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private lateinit var viewModel: FeedViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getProgressBar(): View = progress_bar

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

        (requireActivity().application as MovieFinderApp).component.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(FeedViewModel::class.java)

        searchBarSetting()
    }

    override fun onStart() {
        super.onStart()
        observerUiLiveData()

    }

    private fun searchBarSetting() {
        val dis = search_toolbar.getSearchPublishSubject()
            .subscribe({ Timber.d(it) }, { Timber.e(it) })
        cd.add(dis)
    }

    private fun observerUiLiveData() {
        viewModel.uiStateLiveData().observe(viewLifecycleOwner, { states ->
            states.forEach { state ->
                when (state) {
                    FeedViewModel.ViewState.ErrorState -> {
                    }
                    FeedViewModel.ViewState.HideLoadingViewState -> hideProgressBar()
                    is FeedViewModel.ViewState.OnSuccessState -> {
                        val o = state.onSuccessData
                    }
                    FeedViewModel.ViewState.ShowLoadingViewState -> showProgressBar()
                }
            }
        })
    }

    private fun moviesLoaded(results: HashMap<BlockMovies, MovieResponse>) {
        results.keys.forEach {
            results[it]?.results?.let { movies ->
                addBlockToAdapter(
                    movies,
                    resolveTitle(it)
                )
            }
        }
    }

    private fun resolveTitle(type: BlockMovies) = when (type) {
        BlockMovies.UPCOMING -> R.string.upcoming
        BlockMovies.POPULAR -> R.string.popular
        BlockMovies.NOW_PLAYING -> R.string.nowPlaying
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
        const val KEY_TITLE = "title"
        const val KEY_SEARCH = "search"
        private const val movieId = "movieId"
    }

    enum class BlockMovies {
        POPULAR, NOW_PLAYING, UPCOMING
    }
}
