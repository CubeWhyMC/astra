package org.cubewhy.astra.plugins

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.cubewhy.astra.configs.ConfigManager
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.functions

class AstraBridgeImpl(private val plugin: Plugin) : AstraBridge() {
    private val JSON = Json { ignoreUnknownKeys = true }

    private val configKey = plugin::class.java.name

    @Suppress("UNCHECKED_CAST", "UNNECESSARY_NOT_NULL_ASSERTION")
    override fun <T> loadConfig(defaltValue: T): T {
        val json = ConfigManager.config.pluginConfigs[configKey] ?: return defaltValue
        defaltValue ?: throw NullPointerException("Default value cannot be null")
        val serializer = defaltValue!!::class.companionObject!!.functions.first { it.name == "serializer" }
            .call(defaltValue!!::class.companionObjectInstance) as KSerializer<T>
        return JSON.decodeFromJsonElement(serializer, json)
    }

    @Suppress("UNCHECKED_CAST", "UNNECESSARY_NOT_NULL_ASSERTION")
    override fun <T> saveConfig(config: T) {
        // get serializer via reflection
        config ?: throw NullPointerException("Config cannot be null")
        val serializer = config!!::class.companionObject!!.functions.first { it.name == "serializer" }
            .call(config!!::class.companionObjectInstance) as KSerializer<T>
        ConfigManager.config.pluginConfigs[configKey] = JSON.encodeToJsonElement(serializer, config)
    }
}