package com.augistlk.irklavimas.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.wear.compose.material3.FilledIconButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButtonDefaults
import com.augistlk.irklavimas.R

@Composable
fun MainMenuScreen(
    onStartClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        FilledIconButton(
            onClick = onStartClick,
            modifier = Modifier
                .size(IconButtonDefaults.LargeButtonSize)
        ) {
            Icon(
                painter = painterResource(R.drawable.start_icon),
                contentDescription = "Start",
                modifier = Modifier
                    .size(IconButtonDefaults.LargeIconSize)
            )
        }
        FilledIconButton(
            onClick = onSettingsClick,
            modifier = Modifier.size(IconButtonDefaults.LargeButtonSize)
        ) {
            Icon(
                painter = painterResource(R.drawable.settings_24dp),
                contentDescription = "Start",
                modifier = Modifier.size(IconButtonDefaults.LargeIconSize)
            )
        }
    }
}