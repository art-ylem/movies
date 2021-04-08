package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("adult")
    val adult: Boolean?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("genre_ids")
    val genreIds: List<Int>?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("original_language")
    val originalLanguage: String?,
    @SerializedName("original_title")
    val originalTitle: String?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("popularity")
    val popularity: Double?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("video")
    val video: Boolean?,
    @SerializedName("vote_average")
    val rating: Double?,
    @SerializedName("vote_count")
    val voteCount: Int?,

    // for tv shows model
    @SerializedName("first_air_date")
    val firstAirDate: String?,
    @SerializedName("name")
    val tvShowTitle: String?,
    @SerializedName("original_name")
    val tvShowOriginalTitle: String?,
    @SerializedName("origin_country")
    val originCountry: List<String?>?

)
