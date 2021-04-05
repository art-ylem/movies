package ru.androidschool.intensiv.data


data class MovieInfo(
    var title: String? = "",
    var description: String? = "",
    var img: Int,
    var isFavorite: Boolean,
    var actors: List<Actor>,
    var params: List<MovieParameter>,
    var voteAverage: Double = 0.0
) {
    val rating: Float
        get() = voteAverage.div(2).toFloat()
}
