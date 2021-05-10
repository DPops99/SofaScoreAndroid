package com.example.proba.room.dao


import androidx.room.*
import com.example.proba.network.model.City

@Dao
interface CityDao {

    @Query("SELECT * FROM cities WHERE isRecent = 1")
    suspend fun getAllCities() : List<City>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cities : List<City>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city :City)

    @Query("SELECT * FROM cities WHERE isFavorite = 1 ORDER BY cOrder DESC")
    suspend fun getFavoriteCities() : List<City>

    @Query("SELECT MAX(cOrder) FROM cities")
    suspend fun getLagrestOrder() : Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(cities : List<City>)

    @Query("DELETE FROM cities WHERE isRecent = 1 and isFavorite=0")
    suspend fun deleteRecentCities()

    @Query("DELETE FROM cities WHERE isFavorite = 1 and isRecent=0")
    suspend fun deleteFavoriteCities()


}