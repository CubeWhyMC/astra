package org.cubewhy.astra.configs

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

object ConfigManager {
    private val JSON = Json { ignoreUnknownKeys = true }
    private val logger = KotlinLogging.logger {}

    var configFile: File = getAstraDirectory().resolve("config.json").toFile()
    var config: Config = Config()

    @OptIn(ExperimentalSerializationApi::class)
    fun load() {
        // parse file
        logger.info { "Loading config" }
        try {
            config = JSON.decodeFromStream(configFile.inputStream())
        } catch (e: Exception) {
            logger.error { "Failed to load config, use the default" }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun save() {
        // save config to local
        logger.info { "Saving config" }
        JSON.encodeToStream(this.config, configFile.outputStream())
    }
}

fun getAstraDirectory(): Path {
    val userHome = System.getProperty("user.home")
    return Paths.get(userHome, ".cubewhy", "lunarcn", "astra")
}