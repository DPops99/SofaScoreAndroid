package com.example.proba.main.first_fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.proba.R
import com.example.proba.databinding.CityItemViewBinding
import com.example.proba.helper.ConverterHelper
import com.example.proba.helper.LocationConverter
import com.example.proba.helper.WrapperHelperObject
import com.example.proba.network.model.City
import java.lang.Math.round

class SearchAdapter(var cities : ArrayList<City>, val context : Context, var listener: SearchAdapter.OnItemClickListener?, var view_listener: OnItemViewClickListener?)  : RecyclerView.Adapter<SearchAdapter.SearchHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        return SearchHolder(CityItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        holder.binding.cityName.text = cities[position].title
        holder.binding.cityDistance.text = cities[position].timezone_name

        val ll = cities[position].latt_long!!.split(",")
        holder.binding.cityLattLong.text = LocationConverter.latitudeAsDMS(ll[0].toDouble(), 2)+" "+LocationConverter.longitudeAsDMS(ll[1].toDouble() , 2)


        holder.binding.cityTemp.text = ConverterHelper.temp(cities[position].consolidated_weather?.get(0)?.the_temp, context).toString()+"°"

        holder.binding.cityWImg.load(WrapperHelperObject.getDrawableResursFromString(cities[position].consolidated_weather?.get(0)?.weather_state_abbr,context))

        if (cities[position].favorite)
            holder.binding.imgStar.load(R.drawable.ic_star_1)
        else
            holder.binding.imgStar.load(R.drawable.ic_star_0)

        if (listener!=null){
            holder.binding.imgStar.setOnClickListener {


                    if (cities[position].favorite){
                        holder.binding.imgStar.load(R.drawable.ic_star_0)
                    }
                    else
                        holder.binding.imgStar.load(R.drawable.ic_star_1)
                    listener?.onItemClick(position, !cities[position].favorite)
            }
        }


    }

    override fun getItemCount(): Int {
        return cities.size
    }

    inner class SearchHolder(val binding: CityItemViewBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        init {
            binding.root.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position!= RecyclerView.NO_POSITION)
                view_listener?.onItemClick(position)

        }
    }


    interface OnItemClickListener{
        fun onItemClick(position: Int, isFav : Boolean)
    }

    interface OnItemViewClickListener{
        fun onItemClick(position: Int)
    }

    fun moveItem(from : Int, to : Int){
        val from_fav = cities[from]
        val from_order = cities[from].order
        val to_order = cities[to].order

        cities[from].order = to_order
        cities[to].order = from_order

        cities[from] = cities[to]
        cities[to] = from_fav


    }

}