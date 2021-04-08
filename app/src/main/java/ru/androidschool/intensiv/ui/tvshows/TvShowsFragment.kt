package ru.androidschool.intensiv.ui.tvshows

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.tv_shows_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.data.MovieResponse
import ru.androidschool.intensiv.network.MovieApiClient
import timber.log.Timber

class TvShowsFragment : Fragment(R.layout.tv_shows_fragment) {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    // сделать BaseFragment - родителя для всех фрагментов, и туда вынести options?
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
    }

    private fun intiRecyclerView() {
        val tvShows = MovieApiClient.apiClient.getTvShow(API_KEY)
        tvShows.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(
                call: Call<MovieResponse>,
                response: Response<MovieResponse>
            ) {
                moviesLoaded(response.body()?.results)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Timber.e(t)
            }
        })
    }

    private fun moviesLoaded(results: List<Movie>?) {
        results?.let { list ->
            val tvShows = list.map { movie -> TvShowItem(movie) }.toList()
            //тут вылетит, если не дождаться получения ответа с сервера и отобразить. Стоит viewBinding использовать?
            tv_show_recycler_view.adapter = adapter.apply { addAll(tvShows) }
        }
    }

    companion object {
        private const val API_KEY = BuildConfig.THE_MOVIE_DATABASE_API
    }
}
