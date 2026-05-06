/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up-to-date changes to the libraries and their usages.
 */

package com.augistlk.irklavimas.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import androidx.wear.compose.ui.tooling.preview.WearPreviewFontScales
import com.augistlk.irklavimas.presentation.theme.IrklavimasTheme
import com.augistlk.irklavimas.presentation.ui.MainMenuScreen

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
                    navController.navigate(Settings.route)
                }
            )
        }
    }
}

@WearPreviewDevices
@WearPreviewFontScales
@Composable
fun DefaultPreview() {
    WearApp()
}