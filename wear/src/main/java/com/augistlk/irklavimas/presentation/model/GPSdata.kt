package com.augistlk.irklavimas.presentation.model

data class GPSdata(
    val time: String,
    val latitude: Double,
    val longitude: Double
)

var GPSroute = mutableListOf<GPSdata>()