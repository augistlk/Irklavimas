package com.augistlk.irklavimas.presentation.model

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

enum class PaceSetting{
    speed, pace
}

data class PaceOption(
    val text: String,
    val option: PaceSetting
)

enum class EnergySaverSetting{
    off, low, high
}

data class EnergySaverOption (
    val text: String,
    val option: EnergySaverSetting
)

@Serializable
data class Settings(
    val paceSetting: PaceSetting = PaceSetting.pace,
    val energySaverSetting: EnergySaverSetting = EnergySaverSetting.off
)

object SettingsSerializer : Serializer<Settings> {

    override val defaultValue: Settings = Settings(
        paceSetting = PaceSetting.pace
    )

    override suspend fun readFrom(input: InputStream): Settings =
        try {
            Json.decodeFromString<Settings>(
                input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read Settings", serialization)
        }

    override suspend fun writeTo(t: Settings, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(t)
                    .encodeToByteArray()
            )
        }
    }
}
