package com.example.proba.helper

import android.location.Location
import android.util.Log
import kotlin.math.absoluteValue

object LocationConverter {

    fun latitudeAsDMS(latitude: Double, decimalPlace: Int): String {
        val direction = if (latitude > 0) "N" else "S"
        var strLatitude = Location.convert(latitude.absoluteValue, Location.FORMAT_SECONDS)
        strLatitude = replaceDelimiters(strLatitude, decimalPlace)
        strLatitude += direction
        return strLatitude
    }

    fun longitudeAsDMS(longitude: Double, decimalPlace: Int): String {
        val direction = if (longitude > 0) "W" else "E"
        var strLongitude = Location.convert(longitude.absoluteValue, Location.FORMAT_SECONDS)
        strLongitude = replaceDelimiters(strLongitude, decimalPlace)
        strLongitude += direction
        return strLongitude
    }

    private fun replaceDelimiters(str: String, decimalPlace: Int): String {
        val spliter = str.split(":")
        Log.d("DANIJEL",str)
        val result = spliter[0]+"°"+spliter[1]+"'"
        return result
    }
}