/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.augistlk.irklavimas.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButtonDefaults
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import androidx.wear.compose.ui.tooling.preview.WearPreviewFontScales
import com.augistlk.irklavimas.R
import com.augistlk.irklavimas.presentation.theme.IrklavimasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp() {
    IrklavimasTheme {
        AppScaffold {
            ScreenScaffold{
                Row(modifier = Modifier
                    .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    ) {
                    Button(modifier = Modifier
                        .padding(16.dp)
                        .size(IconButtonDefaults.LargeButtonSize),
                        onClick = {/* TODO: */ }) {
                        Icon(
                            painter = painterResource(R.drawable.start_icon),
                            contentDescription = "Start",
                            modifier = Modifier
                                .size(IconButtonDefaults.LargeIconSize)
                                .wrapContentSize(align = Alignment.Center)
                        )
                    }
                    Button(modifier = Modifier
                        .padding(16.dp)
                        .size(IconButtonDefaults.LargeButtonSize),
                        onClick = {/* TODO: */ }) {
                        Icon(
                            painter = painterResource(R.drawable.settings_24dp),
                            contentDescription = "Settings",
                            modifier = Modifier
                                .size(IconButtonDefaults.LargeIconSize)
                                .wrapContentSize(align = Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

@WearPreviewDevices
@WearPreviewFontScales
@Composable
fun DefaultPreview() {
    WearApp()
}