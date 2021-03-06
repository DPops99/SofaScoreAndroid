package com.example.proba.main.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proba.R
import com.example.proba.main.model.TetaMenza
import com.example.proba.main.model.enums.Job_Position
import com.example.proba.main.model.enums.Workplace

class CustomViewModel : ViewModel() {

    val tete = MutableLiveData<ArrayList<TetaMenza>>()

    init {
        tete.value = arrayListOf(
            TetaMenza("Marija","Maric", Workplace.SD_CVJETNO_NASELJE,45,12345678,"female", Job_Position.ASSISTANT_CHEF, "first", "fake_user@gmail.com", "Ivanicgradska 5", "https://static.thenounproject.com/png/17239-200.png"),
            TetaMenza("Marin","Maric", Workplace.CASSANDRA,30,12345678,"male", Job_Position.CHEF, "first", "fake_user@gmail.com", "Dubrovacka 4","https://static.thenounproject.com/png/17241-200.png"),
            TetaMenza("Ivo","Ivic", Workplace.FFZG,55,123454448,"male", Job_Position.CASHIER, "second", "fake_user@gmail.com", "Ivanicgradska 14","https://static.thenounproject.com/png/17241-200.png")
        )
    }

    fun addTeta(teta : TetaMenza){
        tete.value?.add(teta)
    }

    fun deleteTeta(teta : TetaMenza){
        tete.value?.remove(teta)
    }
}