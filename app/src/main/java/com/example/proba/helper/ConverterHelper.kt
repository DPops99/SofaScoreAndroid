package com.example.proba.helper

import android.content.Context
import android.preference.Preference
import android.preference.PreferenceManager
import kotlin.math.round

object ConverterHelper {

    const val PREF_METRIC_CODE = "PREF_METRIC_CODE"
    const val DEFAULT_METRIC = "METRIC"


    fun getPreferedMetric(context: Context) = context.getSharedPreferences(PREF_METRIC_CODE, Context.MODE_PRIVATE)

    fun setPreferedMetric(context: Context, metric : String) = context.getSharedPreferences(PREF_METRIC_CODE,Context.MODE_PRIVATE).edit().putString(PREF_METRIC_CODE, metric).apply()


    fun kmToMiles(value : Float){

    }

    fun windSpeed(speed : Float, context: Context):Int{
        if (getPreferedMetric(context).getString(PREF_METRIC_CODE, DEFAULT_METRIC)== DEFAULT_METRIC)
            return round(speed).toInt()
        else
            return (round(speed)*0.62).toInt()
    }

    fun temp(temp : Float, context: Context):Int{
        if (getPreferedMetric(context).getString(PREF_METRIC_CODE, DEFAULT_METRIC)== DEFAULT_METRIC)
            return temp.toInt()
        else
            return (temp/0.5556+32).toInt()
    }

    fun visibility(vis : Float, context: Context):Int{
        if (getPreferedMetric(context).getString(PREF_METRIC_CODE, DEFAULT_METRIC)== DEFAULT_METRIC)
            return round(vis).toInt()
        else
            return round(vis*0.62137).toInt()
    }
}