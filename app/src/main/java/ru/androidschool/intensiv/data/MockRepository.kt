package ru.androidschool.intensiv.data

import androidx.annotation.DrawableRes

object MockRepository {

    fun getMovies(): List<Movie> {

        val moviesList = mutableListOf<Movie>()
        for (x in 0..10) {
            val movie = Movie(
                title = "Spider-Man $x",
                voteAverage = 10.0 - x
            )
            moviesList.add(movie)
        }

        return moviesList
    }

    private fun getActors(count: Int, @DrawableRes img: Int): List<Actor> {

        val actorsList = mutableListOf<Actor>()
        for (x in 0..count) {
            val actor = Actor(
                name = "Actor $x",
                img = img
            )
            actorsList.add(actor)
        }

        return actorsList
    }

    private fun getMovieParameters(): List<MovieParameter> {
        val param1 = MovieParameter(
            title = "Студия",
            value = "Warner Bros."
        )
        val param2 = MovieParameter(
            title = "Жанр",
            value = "Action, Adventure, Fantasy "
        )
        val param3 = MovieParameter(
            title = "Год",
            value = "2018"
        )
        return mutableListOf(param1, param2, param3)
    }


    fun getMovieInfo(count: Int, @DrawableRes img: Int): MovieInfo {
        return MovieInfo(
            title = "Aquamen",
            description = "In 1985 Maine, lighthouse keeper Thomas Curry rescues Atlanna, the queen of the underwater nation of Atlantis, during a storm. They eventually fall in love and have a son named Arthur, who is born with the power to communicate with marine lifeforms. ",
            img = img,
            isFavorite = false,
            actors = getActors(count, img),
            params = getMovieParameters(),
            voteAverage = 8.0
        )
    }
}
