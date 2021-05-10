package com.example.proba.room.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proba.network.model.City
import com.example.proba.room.database.AppDatabase
import kotlinx.coroutines.launch

class RoomViewModel(context: Context): ViewModel() {


    val cities = MutableLiveData<List<City>>()
    val fav_cities = MutableLiveData<List<City>>()
    val roomDB : AppDatabase


    init {
        this.roomDB = AppDatabase.getInstance(context)!!
        getCities()
        getFavCities()
    }



    fun getCities(){
        viewModelScope.launch {
            cities.value = roomDB.cityDao().getAllCities()
        }
    }

    fun saveAndGetFavCity(apiCity: City, isFav : Boolean){
        viewModelScope.launch {

            var maxIndex : Int? = roomDB.cityDao().getLagrestOrder()
            maxIndex?.let {
                apiCity.order = if (isFav) maxIndex + 1 else 0
            }
            roomDB.cityDao().insertCity(apiCity)
            fav_cities.value = roomDB.cityDao().getFavoriteCities()
            Log.d("FAVCITY_SAVED",fav_cities.value.toString())
        }
    }


    fun saveAndGetCities(apiCity: City){
        viewModelScope.launch {
            roomDB.cityDao().insertCity(apiCity)
            cities.value = roomDB.cityDao().getAllCities()
            Log.d("CITY_SAVED",cities.value.toString())
        }
    }


    fun getFavCities(){
        viewModelScope.launch {
            fav_cities.value = roomDB.cityDao().getFavoriteCities()
            Log.d("FAVCITY_SAVED",fav_cities.value.toString())
        }
    }

    fun updateFavCities(cities : List<City>){
        viewModelScope.launch {
            roomDB.cityDao().update(cities)
            fav_cities.value = roomDB.cityDao().getFavoriteCities()
            Log.d("FAVCITY_SAVED",fav_cities.value.toString())
        }
    }

    fun deleteRecentCities(){
        viewModelScope.launch {
            roomDB.cityDao().deleteRecentCities()
            cities.value = roomDB.cityDao().getAllCities()
            cities.value!!.forEach {
                if(it.recent == true)
                    it.recent = false
            }
            roomDB.cityDao().insertAll(cities.value!!)

            cities.value = roomDB.cityDao().getAllCities()
        }
    }

    fun deleteFavoriteCities(){
        viewModelScope.launch {
            roomDB.cityDao().deleteFavoriteCities()
            fav_cities.value = roomDB.cityDao().getFavoriteCities()
        }
    }


}