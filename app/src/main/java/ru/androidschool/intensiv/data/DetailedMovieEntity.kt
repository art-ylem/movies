package ru.androidschool.intensiv.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishList")
data class DetailedMovieEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "movie img")
    val posterPath: String?
)
