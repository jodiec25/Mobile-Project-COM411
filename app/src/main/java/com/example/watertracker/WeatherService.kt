package com.example.watertracker

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

@SuppressLint("MissingPermission")
suspend fun fetchTemperatureHere(context: Context): Double? = withContext(Dispatchers.IO) {
    try {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = listOf(LocationManager.NETWORK_PROVIDER, LocationManager.PASSIVE_PROVIDER)
            .mapNotNull { runCatching { lm.getLastKnownLocation(it) }.getOrNull() }
            .maxByOrNull { it.time }
            ?: return@withContext null

        val url = URL(
            "https://api.open-meteo.com/v1/forecast" +
                    "?latitude=${location.latitude}&longitude=${location.longitude}&current=temperature_2m"
        )
        val connection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        val text = connection.inputStream.bufferedReader().use { it.readText() }
        JSONObject(text).getJSONObject("current").getDouble("temperature_2m")
    } catch (e: Exception) {
        null
    }
}