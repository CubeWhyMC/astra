package org.cubewhy.astra.plugins

import java.awt.Component

abstract class Plugin {
    abstract val name: String
    open val version: String = "UNKNOWN"
    open val description: String? = ""
    lateinit var bridge: AstraBridge

    open fun configPage(): Component? = null
}

enum class PluginState {
    ENABLED, DISABLED
}