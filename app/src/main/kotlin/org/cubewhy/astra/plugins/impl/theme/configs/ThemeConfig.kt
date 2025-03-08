package org.cubewhy.astra.plugins.impl.theme.configs

import kotlinx.serialization.Serializable

@Serializable
data class ThemeConfig(
    var theme: String = "dark"
)
