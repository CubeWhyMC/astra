package org.cubewhy.astra.configs

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val disabledPlugins: List<String> = emptyList(),
)
