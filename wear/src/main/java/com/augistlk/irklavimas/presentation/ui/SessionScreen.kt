package com.augistlk.irklavimas.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Text
import com.augistlk.irklavimas.presentation.dataStore
import com.augistlk.irklavimas.presentation.fusedLocationClient
import com.augistlk.irklavimas.presentation.locationCallback
import com.augistlk.irklavimas.presentation.model.EnergySaverSetting
import com.augistlk.irklavimas.presentation.model.PaceSetting
import com.augistlk.irklavimas.presentation.model.Settings
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun SessionScreen(){
    var startGPSString by remember { mutableStateOf("NO GPS") }
    val context = LocalContext.current
    val settings by context.dataStore.data.collectAsState(Settings(loaded = false))
    val permissionArray = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

    // Initialize the permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){

    }

    // Use LaunchedEffect to check and request permissions when the screen opens
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(permissionArray)
        }
    }

    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        startGPSString = "GPS OK"
    }

    val startTime = remember { SystemClock.elapsedRealtime() }

    val view = LocalView.current
    DisposableEffect(Unit) {
        view.keepScreenOn = true
        onDispose {
            view.keepScreenOn = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Trukmė")
            ElapsedTimeChip(startTime)
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val tempasString = if (settings.paceSetting == PaceSetting.pace) {
                "Tempas (min/km)"
            } else {
                "Greitis (km/h)"
            }
            Text(text = tempasString)
            TempasChip(
                startGPSString = startGPSString,
                settings = settings
            )
        }
    }
}

@Composable
fun ElapsedTimeChip(startTime: Long){
    var elapsedTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            elapsedTime = SystemClock.elapsedRealtime() - startTime
            delay(1000.milliseconds)
        }
    }
    val hours = elapsedTime / 1000 / 60 / 60
    val minutes = elapsedTime / 1000 / 60 % 60
    val seconds = elapsedTime / 1000 % 60
    val timeString: String = if (hours == 0L) {
        "%02d:%02d".format(minutes, seconds)
    }
    else {
        "%02d:%02d:%02d".format(hours, minutes, seconds)
    }

    Chip(
        onClick = {},
        colors = ChipDefaults.chipColors(),
        border = ChipDefaults.chipBorder()
    ){
        Text(text = timeString, fontSize = 36.sp)
    }
}

@Composable
fun TempasChip(
    startGPSString: String,
    settings: Settings
){
    val context = LocalContext.current
    var resultString by remember { mutableStateOf(startGPSString) }
    var debugString by remember { mutableStateOf("DEBUG") }
    var GPSInterval: Long
    var lastLocation = Location("")

    if (settings.loaded) {
        GPSInterval = when(settings.energySaverSetting){
            EnergySaverSetting.off -> 1000
            EnergySaverSetting.low -> 2000
            EnergySaverSetting.high -> 3000
        }

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, GPSInterval)
            .setMinUpdateIntervalMillis(GPSInterval)
            .build()

        Log.i("GPSInterval", GPSInterval.toString())

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation ?: return
                if (location.hasSpeed() && location.speed > 0.1f) {
                    val speed = location.speed
                    val pace = 16.6666667 / speed
                    val minutes = pace.toInt()
                    val seconds = ((pace % 1) * 60).toInt()

                    if (lastLocation.provider != "" && settings.debug) {
                        val debugSpeed = lastLocation.distanceTo(location) / ((location.time - lastLocation.time)/1000)
                        val debugPace = 16.6666667 / debugSpeed
                        val debugMinutes = debugPace.toInt()
                        val debugSeconds = ((debugPace % 1) * 60).toInt()

                        Log.i("debugSpeed", debugSpeed.toString())

                        if ((speed - debugSpeed).absoluteValue > 1f) {
                            Log.e("Speed mismatch", "Speed difference between speed value and distance over time over 1m/s")
                        }
                        @Suppress("KotlinConstantConditions")
                        if (settings.debug) {
                            debugString = if (settings.paceSetting == PaceSetting.pace) {
                                "%02d:%02d".format(debugMinutes, debugSeconds)
                            } else {
                                "%.1f".format(debugSpeed * 3.6)
                            }
                        }
                    }

                    resultString = if (settings.paceSetting == PaceSetting.pace) {
                        "%02d:%02d".format(minutes, seconds)
                    } else {
                        "%.1f".format(speed * 3.6)
                    }
                } else {
                    resultString = if (settings.paceSetting == PaceSetting.pace) {
                        "--:--"
                    } else {
                        "-.-"
                    }
                }
                lastLocation = location
            }
        }

        DisposableEffect(Unit) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
            onDispose {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
    }

    if (!settings.debug) {
        Chip(
            onClick = {},
            colors = ChipDefaults.chipColors(),
            border = ChipDefaults.chipBorder()
        ) {
            Text(text = resultString, fontSize = 36.sp)
        }
    } else {
        Chip(
            onClick = {},
            colors = ChipDefaults.chipColors(),
            border = ChipDefaults.chipBorder()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = resultString)
                VerticalDivider()
                Text(text = debugString)
            }
        }
    }
}