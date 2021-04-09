package com.example.proba.singleCity.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.proba.R
import com.example.proba.databinding.ChildItemBinding
import com.example.proba.network.model.Day
import com.example.proba.singleCity.model.ChildModel
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class ChildAdapter(private val children : List<Day>, private val title: String, private val context : Context) : RecyclerView.Adapter<ChildAdapter.ChildHolder>() {


    inner class ChildHolder(val binding : ChildItemBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildHolder {
        return ChildHolder(ChildItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: ChildHolder, position: Int) {

        val date = SimpleDateFormat("dd-MM-yyyy").parse(children[position].applicable_date!!)

        holder.binding.ciValue.text = children[position].the_temp?.toInt().toString()+"°"


        var id = "@drawable/ic_"+children[position].weather_state_abbr
        var img_res = context.resources.getIdentifier(id,null,context.packageName)
        var logo_draw = context.resources.getDrawable(img_res)
        holder.binding.ciImg.load(logo_draw){
            placeholder(R.drawable.ic_wind)
        }
        holder.binding.ciTitle.text = if (title == "Today") "07:00" else SimpleDateFormat("EE",Locale.getDefault()).format(date.time).toString().toUpperCase()
    }

    override fun getItemCount(): Int {
        return children.size
    }
}