package ru.androidschool.intensiv.data

import androidx.annotation.DrawableRes

object MockRepository {

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
}
