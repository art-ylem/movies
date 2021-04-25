package ru.androidschool.intensiv.room

import androidx.room.*
import io.reactivex.Single
import ru.androidschool.intensiv.data.dto.DetailedMovieEntity

@Dao
interface WishListMovieDAO {
    @Query("SELECT * FROM wishList")
    fun getAll(): Single<List<DetailedMovieEntity>>

    @Insert
    fun insert(data: DetailedMovieEntity)

    @Update
    fun update(data: DetailedMovieEntity)

    @Delete
    fun delete(vararg data: DetailedMovieEntity)
}
