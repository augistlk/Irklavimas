package com.augistlk.irklavimas.presentation.ui

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.rotary.rotaryScrollable
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.RadioButton
import androidx.wear.compose.material.Text
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import androidx.wear.compose.ui.tooling.preview.WearPreviewFontScales
import com.augistlk.irklavimas.R
import com.augistlk.irklavimas.presentation.dataStore
import com.augistlk.irklavimas.presentation.model.EnergySaverOption
import com.augistlk.irklavimas.presentation.model.EnergySaverSetting
import com.augistlk.irklavimas.presentation.model.PaceOption
import com.augistlk.irklavimas.presentation.model.PaceSetting
import com.augistlk.irklavimas.presentation.model.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val maxChipTextFontSize = 20.sp

enum class ShownDialog{
    None, Energy, Pace, Debug
}

@Composable
fun SettingsScreen() {
    var currentDialog by remember { mutableStateOf(ShownDialog.None) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val settings by context.dataStore.data.collectAsState(initial = Settings())
    ScalingLazyColumn(
        Modifier
            .fillMaxSize()
    ) {
        item {
            Text(
                text = "Nustatymai",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            )
        }
        item {
            SettingsChip(
                iconResource = R.drawable.energy_savings_leaf_24dp,
                text = "Energijos Taupymas",
                onClick = {currentDialog = ShownDialog.Energy}
            )
        }
        item {
            SettingsChip(
                iconResource = R.drawable.pace_24dp,
                text = "Tempas",
                onClick = {currentDialog = ShownDialog.Pace}
            )
        }
        item {
            SettingsChip(
                iconResource = R.drawable.bug_report_24dp,
                text = "Debug",
                onClick = {currentDialog = ShownDialog.Debug}
            )
        }
    }

    if (currentDialog == ShownDialog.Energy) {
        EnergySaverDialog(
            onDismiss = { currentDialog = ShownDialog.None },
            coroutineScope = coroutineScope,
            settings = settings,
            context = context
        )
    }
    if (currentDialog == ShownDialog.Pace) {
        PaceDialog(
            onDismiss = { currentDialog = ShownDialog.None },
            coroutineScope = coroutineScope,
            settings = settings,
            context = context
        )
    }
    if (currentDialog == ShownDialog.Debug) {
        DebugDialog(
            onDismiss = { currentDialog = ShownDialog.None },
            coroutineScope = coroutineScope,
            settings = settings,
            context = context
        )
    }
}

@Composable
fun SettingsChip(
    @DrawableRes iconResource: Int,
    text: String,
    onClick: () -> Unit,
){
    Chip(
        onClick = onClick,
        colors = ChipDefaults.chipColors(),
        border = ChipDefaults.chipBorder(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxHeight()
        ) {
            Icon(
                painter = painterResource(iconResource),
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            BasicText(
                text = text,
                autoSize = TextAutoSize.StepBased(
                    maxFontSize = maxChipTextFontSize
                ),
                style = TextStyle(fontWeight = FontWeight.Bold),
                softWrap = false
            )
        }
    }
}

@Composable
fun EnergySaverDialog(
    onDismiss: () -> Unit,
    coroutineScope: CoroutineScope,
    settings: Settings,
    context: Context
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {

        val energySaverOptions = listOf(
            EnergySaverOption("Išjungtas", EnergySaverSetting.off),
            EnergySaverOption("Mažas", EnergySaverSetting.low),
            EnergySaverOption("Didelis", EnergySaverSetting.high)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .selectableGroup()
                .fillMaxSize()
                .background(color = Color.Black)
        ) {
            energySaverOptions.forEach { option ->
                Row(
                    Modifier.fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (option.option == settings.energySaverSetting),
                            onClick = {
                                coroutineScope.launch {
                                    updateSettings(context = context){it.copy(energySaverSetting = option.option)}
                                }
                                onDismiss()
                            },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (option.option == settings.energySaverSetting),
                        onClick = null // null recommended for accessibility with screen readers
                    )
                    Text(text = option.text, modifier = Modifier.padding(start = 16.dp))
                }
            }
        }
    }
}

@Composable
fun PaceDialog(
    onDismiss: () -> Unit,
    coroutineScope: CoroutineScope,
    settings: Settings,
    context: Context
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        val paceOptions = listOf(
            PaceOption("Tempas", PaceSetting.pace),
            PaceOption("Greitis", PaceSetting.speed)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .selectableGroup()
                .fillMaxSize()
                .background(color = Color.Black)
        ) {
            paceOptions.forEach { option ->
                Row(
                    Modifier.fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (option.option == settings.paceSetting),
                            onClick = {
                                coroutineScope.launch {
                                    updateSettings(context = context) {it.copy(paceSetting = option.option)}
                                }
                                onDismiss()
                            },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (option.option == settings.paceSetting),
                        onClick = null // null recommended for accessibility with screen readers
                    )
                    Text(text = option.text, modifier = Modifier.padding(start = 16.dp))
                }
            }
        }
    }
}

@Composable
fun DebugDialog(
    onDismiss: () -> Unit,
    coroutineScope: CoroutineScope,
    settings: Settings,
    context: Context
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        val debugOptions = listOf(
            Pair("Įjungta", true),
            Pair("Išjungta", false)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .selectableGroup()
                .fillMaxSize()
                .background(color = Color.Black)
        ) {
            debugOptions.forEach { (text, value) ->
                Row(
                    Modifier.fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (value == settings.debug),
                            onClick = {
                                coroutineScope.launch {
                                    updateSettings(context = context) { it.copy(debug = value) }
                                }
                                onDismiss()
                            },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (value == settings.debug),
                        onClick = null
                    )
                    Text(text = text, modifier = Modifier.padding(start = 16.dp))
                }
            }
        }
    }
}

suspend fun updateSettings(
    context: Context,
    transform: (Settings) -> Settings
) {
    try {
        context.dataStore.updateData { currentSettings ->
            transform(currentSettings)
        }
    } catch (e: Exception) {
        throw e
    }
}

@WearPreviewDevices
@WearPreviewFontScales
@Composable
fun SettingsScreenPreview(){
    SettingsScreen()
}