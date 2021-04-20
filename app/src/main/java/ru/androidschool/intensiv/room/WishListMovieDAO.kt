package ru.androidschool.intensiv.room

import androidx.room.*
import io.reactivex.Observable
import io.reactivex.Single
import ru.androidschool.intensiv.data.DetailedMovieRoom

@Dao
interface WishListMovieDAO {
    @Query("SELECT * FROM wishList")
    fun getAll(): Single<List<DetailedMovieRoom>>

    @Insert
    fun insert(data: DetailedMovieRoom)

    @Update
    fun update(data: DetailedMovieRoom)

    @Delete
    fun delete(vararg data: DetailedMovieRoom)

}