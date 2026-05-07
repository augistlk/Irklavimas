package com.augistlk.irklavimas.presentation.model

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import com.augistlk.irklavimas.presentation.model.Settings

@Serializable
data class Settings(
    val firstRowData: String,
    val secondRowData: String
)

object SettingsSerializer : Serializer<Settings> {

    override val defaultValue: Settings = Settings(
        firstRowData = "",
        secondRowData = ""
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
        output.write(
            Json.encodeToString(t)
                .encodeToByteArray()
        )
    }
}
