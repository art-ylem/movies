package ru.androidschool.intensiv.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.androidschool.intensiv.data.DetailedMovieRoom

@Database(entities = [DetailedMovieRoom::class], version = 1)
abstract class AppDB : RoomDatabase() {
    abstract fun movies(): WishListMovieDAO
}