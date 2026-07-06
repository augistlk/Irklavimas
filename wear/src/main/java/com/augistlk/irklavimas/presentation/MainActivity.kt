/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up-to-date changes to the libraries and their usages.
 */

package com.augistlk.irklavimas.presentation

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import androidx.wear.compose.ui.tooling.preview.WearPreviewFontScales
import com.augistlk.irklavimas.presentation.model.Settings
import com.augistlk.irklavimas.presentation.model.SettingsSerializer
import com.augistlk.irklavimas.presentation.theme.IrklavimasTheme
import com.augistlk.irklavimas.presentation.ui.MainMenuScreen
import com.augistlk.irklavimas.presentation.ui.SessionScreen
import com.augistlk.irklavimas.presentation.ui.SettingsScreen
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices

val Context.dataStore: DataStore<Settings> by dataStore(
    fileName = "settings.json",
    serializer = SettingsSerializer,
)

lateinit var fusedLocationClient: FusedLocationProviderClient
lateinit var locationCallback: LocationCallback

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            WearApp()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun WearApp() {
    IrklavimasTheme {
        val navController = rememberNavController()
        AppScaffold {
            ScreenScaffold{
                innerPadding ->
                    IrklavimasNavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun IrklavimasNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = MainMenu.route,
        modifier = modifier
    ){
        composable(route = MainMenu.route){
            MainMenuScreen(
                onStartClick = {
                    navController.navigate(Session.route)
                },
                onSettingsClick = {
                    navController.navigate(SettingsPanel.route)
                }
            )
        }
        composable(route = SettingsPanel.route){
            SettingsScreen(
                onApply = TODO(),
                onCancel = TODO()
            )
        }
        composable(route = Session.route) {
            SessionScreen()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@WearPreviewDevices
@WearPreviewFontScales
@Composable
fun DefaultPreview() {
    WearApp()
}