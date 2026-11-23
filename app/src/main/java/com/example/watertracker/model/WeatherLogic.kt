package com.example.watertracker.model

fun weatherBonusMl(tempC: Double?): Int = when {
    tempC == null -> 0
    tempC >= 30.0 -> 500
    tempC >= 25.0 -> 250
    else -> 0
}