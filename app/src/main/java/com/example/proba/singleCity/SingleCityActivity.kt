package com.example.proba.singleCity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.proba.R
import com.example.proba.databinding.ActivitySingleCityBinding
import com.example.proba.helper.ConverterHelper
import com.example.proba.helper.WrapperHelperObject
import com.example.proba.main.first_fragment.SearchFragment
import com.example.proba.network.model.City
import com.example.proba.network.model.Day
import com.example.proba.singleCity.adapter.ParentAdapter
import com.example.proba.singleCity.model.ParentModel
import kotlinx.android.synthetic.main.activity_single_city.*
import kotlinx.android.synthetic.main.child_item.*
import java.text.SimpleDateFormat
import kotlin.math.round

class SingleCityActivity : AppCompatActivity() {


    private lateinit var binding : ActivitySingleCityBinding
    private lateinit var current_city : City
    private lateinit var current_day : List<Day>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivitySingleCityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {

            binding.collapsingToolbarLayout.isTitleEnabled = false
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }


        setView()
    }

    private fun setView() {




        intent?.getSerializableExtra(SearchFragment().city_bundle)?.let {

            var wind_metric = ""
            var vis_metric = ""
            if (ConverterHelper.getPreferedMetric(applicationContext).getString(ConverterHelper.PREF_METRIC_CODE, ConverterHelper.DEFAULT_METRIC) == ConverterHelper.DEFAULT_METRIC) {
                wind_metric = " km/h "
                vis_metric = " km "
            }
            else{
                wind_metric = " mph "
                vis_metric = " mi "
            }

            current_city = intent.getSerializableExtra(SearchFragment().city_bundle) as City


            binding.toolbar.title = current_city.title

            val date = SimpleDateFormat("E, MMMM d").format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(current_city.time!!))
            val time = SimpleDateFormat("hh:mm a").format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(current_city.time!!))

            binding.scName.text = current_city.title
            binding.scTemp.text = ConverterHelper.temp(current_city.consolidated_weather[0].the_temp, applicationContext).toString()+"°"
            binding.scWeather.text = current_city.consolidated_weather?.get(0)?.weather_state_name
            binding.scDate.text = date.capitalizeWords()
            binding.scTime.text = """$time (${current_city.timezone_name})"""

            binding.scWeatherImg.load(WrapperHelperObject.getDrawableResursFromString(current_city.consolidated_weather?.get(0)?.weather_state_abbr, applicationContext)){
                placeholder(R.drawable.ic_wind)
            }

            binding.wiTemp.wDesc.text = getString(R.string.min_max_temp)
            binding.wiTemp.wIcon.load(R.drawable.ic_thermostat)
            binding.wiTemp.wValue.text = ConverterHelper.temp(current_city.consolidated_weather.get(0).min_temp, applicationContext).toString() +"° /" +
                    ConverterHelper.temp(current_city.consolidated_weather.get(0).max_temp, applicationContext).toString()+"°"

            binding.wiWind.wDesc.text = getString(R.string.wind)
            binding.wiWind.wIcon.load(R.drawable.ic_wind)
            binding.wiWind.wValue.text = ConverterHelper.windSpeed(current_city.consolidated_weather?.get(0)?.wind_speed, applicationContext).toString() + wind_metric +
                    current_city.consolidated_weather?.get(0)?.wind_direction_compass.toString()

            binding.wiHum.wDesc.text = getString(R.string.humidity)
            binding.wiHum.wIcon.load(R.drawable.ic_humidity)
            binding.wiHum.wValue.text = """${current_city.consolidated_weather?.get(0)?.humidity?.toString()}%"""

            binding.wiPress.wDesc.text = getString(R.string.pressure)
            binding.wiPress.wIcon.load(R.drawable.ic_pressure)
            binding.wiPress.wValue.text = """${round(current_city.consolidated_weather?.get(0)?.air_pressure!!).toInt()} hPa"""

            binding.wiVis.wDesc.text = getString(R.string.visibilty)
            binding.wiVis.wIcon.load(R.drawable.ic_visibility)
            binding.wiVis.wValue.text = ConverterHelper.visibility(current_city.consolidated_weather?.get(0)?.visibility, applicationContext).toString()+vis_metric


            binding.wiAcc.wDesc.text = getString(R.string.accuracy)
            binding.wiAcc.wIcon.load(R.drawable.ic_accuracy)
            binding.wiAcc.wValue.text =  """${current_city.consolidated_weather?.get(0)?.predictability?.toString()}%"""


        }

        intent?.getSerializableExtra(SearchFragment().day_bundle)?.let{
            current_day = intent.getSerializableExtra(SearchFragment().day_bundle) as List<Day>
        }





        binding.scRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
            adapter = ParentAdapter(getData(), context)
        }
    }

    private fun getData() : List<ParentModel>{
        val parents = mutableListOf<ParentModel>()

        parents.add(ParentModel(getString(R.string.today),current_day))
        parents.add(ParentModel(getString(R.string.next_days),current_city.consolidated_weather!!.subList(1, current_city.consolidated_weather!!.size)))

        return parents
    }

    fun String.capitalizeWords() : String = split(" ").joinToString(" "){it.capitalize()}
}