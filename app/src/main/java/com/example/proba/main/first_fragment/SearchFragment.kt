package com.example.proba.main.first_fragment


import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proba.databinding.SearchFragmentBinding
import com.example.proba.main.first_fragment.adapter.SearchAdapter
import com.example.proba.main.view_model.ApiViewModel
import com.example.proba.network.model.City
import com.example.proba.network.model.Day
import com.example.proba.network.model.Search
import com.example.proba.room.database.AppDatabase
import com.example.proba.room.model.Favorite
import com.example.proba.room.viewmodel.RoomFactory
import com.example.proba.room.viewmodel.RoomViewModel
import com.example.proba.singleCity.SingleCityActivity
import com.google.android.material.snackbar.Snackbar
import java.io.Serializable

class SearchFragment : Fragment(), SearchAdapter.OnItemLongClickListener, SearchAdapter.OnItemClickListener {

    private val apiViewModel : ApiViewModel by activityViewModels()
    private lateinit var roomViewModel : RoomViewModel
    private lateinit var roomViewModelFactory : RoomFactory
    private lateinit var roomDB : AppDatabase
    private var _binding : SearchFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : SearchAdapter
    private var current_adapter_position : Int? = null
    private lateinit var current_city : City
    val day_bundle = "DAY_BUNDLE"
    val city_bundle = "CITY_BUNDLE"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SearchFragmentBinding.inflate(inflater,container,false)
        val view = binding.root
        roomDB = AppDatabase.getInstance(requireContext())!!
        roomViewModelFactory = RoomFactory(roomDB)
        roomViewModel = ViewModelProvider(this,roomViewModelFactory).get(RoomViewModel::class.java)
        setView()
        return view
    }


    fun setView(){

        binding.searchRecyclerView.layoutManager =  LinearLayoutManager(requireContext())
        adapter = SearchAdapter(ArrayList<City>(),this.requireContext(), this, this)
        binding.searchRecyclerView.adapter = adapter
        apiViewModel.api_search.observe(viewLifecycleOwner, Observer {
            Log.d("QUERY_IS_NULL","outside")
            if (it != null && binding.searchView.query.isNotBlank() && binding.searchView.query.isNotEmpty()) {
                Toast.makeText(this.requireContext(), "Waiting for results",Toast.LENGTH_LONG).show()
                apiViewModel.get_api_cities()
            }
            else if(binding.searchView.query.isNullOrBlank() || binding.searchView.query.isNullOrEmpty()){
                Log.d("QUERY_IS_NULL","inside")
                adapter.cities = ArrayList<City>()
                adapter.notifyDataSetChanged()
            }
        })

        apiViewModel.api_cities.observe(viewLifecycleOwner, Observer {
            if(it != null && binding.searchView.query.isNotBlank() && binding.searchView.query.isNotEmpty()){
                adapter.cities = it
                adapter.notifyDataSetChanged()
                Log.d("BEFORE_LIFTOF",it.toString())
//                roomViewModel.saveAndGetCities(it)
            }
            else{
                adapter.cities = ArrayList<City>()
                adapter.notifyDataSetChanged()
            }
        })


        roomViewModel.cities.observe(viewLifecycleOwner, Observer {
            if(it!=null){
                Log.d("ROOMVIEWMODEL",it.toString())
            }
        })




        apiViewModel.api_day.observe(viewLifecycleOwner, Observer {
            it?.let {
                val intent : Intent = Intent(this.context, SingleCityActivity::class.java)
                intent.putExtra(day_bundle, it as Serializable)
                intent.putExtra(city_bundle, current_city)
                startActivity(intent)
            }
        })


        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!=null) {
                    Log.d("BACK_API","got from search view")
                    if (isNetworkConnected())
                        apiViewModel.get_api_search(query)
//                    else if(query == "")
//                        Toast.makeText(context, "Please enter city name in the search bar",Toast.LENGTH_LONG).show()
                    else
                        Toast.makeText(context, "No internet connection, please connect",Toast.LENGTH_LONG).show()
                }
                return false
            }
            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }
        })

        roomViewModel.fav_cities.observe(viewLifecycleOwner, Observer {
            Log.d("FAVORITE_SAVED",it.toString())
        })


    }


    override fun onItemLongClick(position: Int) {
        if (adapter.cities[position].woeid != null ) {

                    current_city = apiViewModel.api_cities.value?.filter { it.title == adapter.cities[position].title }?.get(0)!!
                    roomViewModel.saveAndGetCities(current_city)
                    current_city.consolidated_weather?.get(0)?.applicable_date?.let {
                        Log.d("DAY_API",adapter.cities[position].woeid.toString())
                        Log.d("DAY_API",it)
                        apiViewModel.get_api_day(adapter.cities[position].woeid!!, it)
                    }
                }
    }

    override fun onItemClick(position: Int, isFav : Boolean) {
        current_city = apiViewModel.api_cities.value?.filter { it.title == adapter.cities[position].title }?.get(0)!!
        if (isFav)
            roomViewModel.saveAndGetFavCities(Favorite(0,current_city))
        else
            roomViewModel.deleteAndGetFavCities(Favorite(0,current_city))
    }



    private fun isNetworkConnected() : Boolean{

        val cm =context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        return isConnected
    }

}