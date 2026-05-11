package com.augistlk.irklavimas.presentation.ui

import android.os.SystemClock
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Text
import kotlinx.coroutines.delay

@Composable
fun SessionScreen(

){
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
            Text(text = "Tempas")
            Chip(
                onClick = {},
                colors = ChipDefaults.chipColors(),
                border = ChipDefaults.chipBorder()
            ) {

            }
        }
    }
}

@Composable
fun ElapsedTimeChip(startTime: Long){
    var elapsedTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            elapsedTime = SystemClock.elapsedRealtime() - startTime
            delay(1000)
        }
    }
    val hours = elapsedTime / 1000 / 60 / 60
    val minutes = elapsedTime / 1000 / 60
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