package com.example.proba.main.second_fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.proba.R
import com.example.proba.databinding.ShowFragmentBinding
import com.example.proba.main.first_fragment.adapter.SearchAdapter
import com.example.proba.main.view_model.ApiViewModel
import com.example.proba.network.model.City
import com.example.proba.room.viewmodel.RoomFactory
import com.example.proba.room.viewmodel.RoomViewModel
import com.example.proba.singleCity.SingleCityActivity
import java.io.Serializable


class ShowFragment : Fragment(), SearchAdapter.OnItemClickListener, SearchAdapter.OnItemViewClickListener{

    private var _binding : ShowFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SearchAdapter
    private lateinit var roomViewModel : RoomViewModel
    private lateinit var roomViewModelFactory : RoomFactory
    private lateinit var current_city : City
    private val apiViewModel : ApiViewModel by activityViewModels()
    val day_bundle = "DAY_BUNDLE"
    val city_bundle = "CITY_BUNDLE"
    private val itemTouchHelper by lazy {

        val simpleItemTouchCallback =
                object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN ,0 ){
                    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {

                        val from = viewHolder.adapterPosition
                        val to = target.adapterPosition

                        adapter.moveItem(from,to)
                        adapter.notifyItemMoved(from, to)
                        return true

                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    }

                }
        ItemTouchHelper(simpleItemTouchCallback)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ShowFragmentBinding.inflate(inflater,container,false)
        val view = binding.root

        roomViewModelFactory = RoomFactory(requireContext())
        roomViewModel = ViewModelProvider(this,roomViewModelFactory).get(RoomViewModel::class.java)
        setView()
        setListeners()
        return view
    }


    fun setView(){
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchAdapter(ArrayList<City>(),this.requireContext(), null, this)
        binding.recyclerView.adapter = adapter
        itemTouchHelper.attachToRecyclerView(null)

        roomViewModel.fav_cities.observe(viewLifecycleOwner, Observer {
            if(it!=null){
                adapter.cities = it as ArrayList<City>
                adapter.notifyDataSetChanged()
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
    }

    fun setListeners(){
        binding.showEdit.setOnClickListener{
            if(binding.showCal.visibility == View.VISIBLE){
                binding.showCal.visibility=View.INVISIBLE
                binding.showEdit.load(R.drawable.ic_baseline_check_24)
                itemTouchHelper.attachToRecyclerView(binding.recyclerView)
                adapter = SearchAdapter(adapter.cities,this.requireContext(), this, null)

            }
            else{
                roomViewModel.updateFavCities(adapter.cities)
                binding.showCal.visibility=View.VISIBLE
                binding.showEdit.load(R.drawable.ic_icon_1)
                itemTouchHelper.attachToRecyclerView(null)
                adapter = SearchAdapter(adapter.cities,this.requireContext(), null, this)
            }
            binding.recyclerView.adapter = adapter
        }
    }

    override fun onItemClick(position: Int, isFav : Boolean) {
        current_city = adapter.cities[position]
        current_city.favorite = isFav
        roomViewModel.saveAndGetFavCity(current_city, isFav)
    }

    override fun onItemClick(position: Int) {

        current_city = adapter.cities[position]
        current_city.consolidated_weather.get(0).applicable_date.let {
            Log.d("DAY_API",adapter.cities[position].woeid.toString())
            Log.d("DAY_API",it)
            apiViewModel.get_api_day(adapter.cities[position].woeid, it)
        }
    }
}