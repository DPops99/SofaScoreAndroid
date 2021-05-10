package com.example.proba.singleCity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.proba.R
import com.example.proba.databinding.ChildItemBinding
import com.example.proba.helper.ConverterHelper
import com.example.proba.helper.WrapperHelperObject
import com.example.proba.network.model.Day
import java.text.SimpleDateFormat
import java.util.*

class ChildAdapter(private val children : List<Day>, private val title: String, private val context : Context) : RecyclerView.Adapter<ChildAdapter.ChildHolder>() {


    inner class ChildHolder(val binding : ChildItemBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildHolder {
        return ChildHolder(ChildItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: ChildHolder, position: Int) {

        val date = SimpleDateFormat("dd-MM-yyyy").parse(children[position].applicable_date!!)
        val time = SimpleDateFormat("HH:mm").format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(children[position].created!!))

        holder.binding.ciValue.text = children[position].the_temp?.toInt().toString()+"°"
        holder.binding.ciValue.text = ConverterHelper.temp(children[position].the_temp, context).toString()+"°"


        holder.binding.ciImg.load(WrapperHelperObject.getDrawableResursFromString(children[position].weather_state_abbr, context)){
            placeholder(R.drawable.ic_wind)
        }
        holder.binding.ciTitle.text = if (title == context.getString(R.string.today)) time else SimpleDateFormat("EE",Locale.getDefault()).format(date.time).toString().toUpperCase()
    }

    override fun getItemCount(): Int {
        return children.size
    }
}