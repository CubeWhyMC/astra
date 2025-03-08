package org.cubewhy.astra.configs

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Config(
    val disabledPlugins: MutableList<String> = mutableListOf(),
    val pluginConfigs: MutableMap<String, JsonElement> = mutableMapOf(),
)
