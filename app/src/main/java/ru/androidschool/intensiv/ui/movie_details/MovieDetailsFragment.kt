package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.movie_details_fragment.*
import kotlinx.android.synthetic.main.movie_details_fragment.description
import kotlinx.android.synthetic.main.movie_param.view.*
import ru.androidschool.intensiv.*
import ru.androidschool.intensiv.data.DetailedMovie
import ru.androidschool.intensiv.data.DetailedMovieRoom
import ru.androidschool.intensiv.data.MovieCredits
import ru.androidschool.intensiv.data.MovieInfo
import ru.androidschool.intensiv.room.AppDB
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MovieDetailsFragment : Fragment(R.layout.movie_details_fragment) {

    private val cd = CompositeDisposable()
    private val likePublishSubject = PublishSubject.create<Boolean>()
    private val adapter by lazy {
        GroupAdapter<com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder>()
    }
    private val db by lazy {
        Room.databaseBuilder(
            requireContext(),
            AppDB::class.java, movieDB
        ).build()
    }
    private var currentMovie: DetailedMovie? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieId = requireArguments().getString(movieId)
        movieId?.let { loadData(it) }
        setToolbar()
    }

    private fun loadData(id: String) {
        // QUESTION: у Single'a придется в двух местах скрывать progress bar? в onError и onSuccess?
        val dis = Single.zip(
            retrofit.movieCreditsByIdRequest(id),
            retrofit.movieInfoByIdRequest(id),
            { movieCredits, movieInfo ->
                DetailedMovie(
                    movieCredits,
                    movieInfo
                )
            })
            .doOnSubscribe { showProgressBar() }
            .myObserve()
            .doOnSuccess { hideProgressBar() }
            .subscribe({
                dataLoaded(it)
            }, {
                hideProgressBar()
                Timber.e(it)
            })

        cd.add(dis)
    }

    private fun dataLoaded(detailedMovie: DetailedMovie?) {
        //QUESTION: так норм сохранять сущность?
        currentMovie = detailedMovie?.copy()

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
        likeBtnListener(checkBox)
        likeBtnObserver()
    }

    private fun likeBtnObserver() {
        val dis = likePublishSubject.debounce(debounceTime, TimeUnit.SECONDS)
            .subscribe { isLiked -> if (isLiked) sendDataToDB() else deleteDataFromDB() }
        cd.add(dis)
    }

    private fun deleteDataFromDB() {
        currentMovie?.let {
            roomEntityMapper(it)?.let { dbData -> db.movies().delete(dbData) }
        }
    }

    private fun sendDataToDB() {
        currentMovie?.let {
            roomEntityMapper(it)?.let { dbData -> db.movies().insert(dbData) }
        }
    }

    private fun roomEntityMapper(data: DetailedMovie) =
        data.info?.id?.let { id ->DetailedMovieRoom(id, data.info.posterPath) }


    private fun likeBtnListener(checkBox: CheckBox) {
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            likePublishSubject.onNext(isChecked)
        }
    }

    private fun setInfo(data: MovieInfo) {
        // set parameters
        setParams(data)

        // set other fields
        title.text = data.title
        description.text = data.overview
        tv_show_item_rating.rating = data.voteAverage?.toRating()!!
        Picasso.get()
            .load(BuildConfig.IMAGE_URL + data.posterPath)
            .into(image_view)
    }

    private fun hideProgressBar() {
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
        currentMovie = null
    }

    companion object {
        //QUESTION: все "magic number" вынести в companion object?
        private const val yearSubstring = 4
        private const val debounceTime = 1L
        private const val movieId = "movieId"
        private const val movieDB = "movie_database"
    }
}
