package org.cubewhy.astra.plugins

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.cubewhy.astra.configs.ConfigManager
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.functions

class AstraBridgeImpl(plugin: Plugin) : AstraBridge() {
    companion object {
        private val logger = KotlinLogging.logger {}
        private val JSON = Json { ignoreUnknownKeys = true }
    }

    private val configKey = plugin::class.java.name

    @Suppress("UNCHECKED_CAST", "UNNECESSARY_NOT_NULL_ASSERTION")
    override fun <T> loadConfig(defaultValue: T): T {
        val json = ConfigManager.config.pluginConfigs[configKey] ?: return defaultValue
        defaultValue ?: throw NullPointerException("Default value cannot be null")
        val serializer = defaultValue!!::class.companionObject!!.functions.first { it.name == "serializer" }
            .call(defaultValue!!::class.companionObjectInstance) as KSerializer<T>
        logger.info { "Load config for plugin $configKey" }
        return JSON.decodeFromJsonElement(serializer, json)
    }

    @Suppress("UNCHECKED_CAST", "UNNECESSARY_NOT_NULL_ASSERTION")
    override fun <T> saveConfig(config: T) {
        // get serializer via reflection
        config ?: throw NullPointerException("Config cannot be null")
        val serializer = config!!::class.companionObject!!.functions.first { it.name == "serializer" }
            .call(config!!::class.companionObjectInstance) as KSerializer<T>
        logger.info { "Save config for plugin $configKey" }
        ConfigManager.config.pluginConfigs[configKey] = JSON.encodeToJsonElement(serializer, config)
    }
}