package ru.androidschool.intensiv.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// QUESTION: просто добавлять к названии сущности 'room' - норм? чтобы понимать что эти классы практически одно и тоже, только в разных местах используются

@Entity(tableName = "wishList")
data class DetailedMovieRoom(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "movie img")
    val posterPath: String?
)
