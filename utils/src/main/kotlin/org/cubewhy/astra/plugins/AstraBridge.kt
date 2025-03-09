package org.cubewhy.astra.plugins

abstract class AstraBridge {
    abstract fun <T> loadConfig(defaultValue: T): T
    abstract fun <T> saveConfig(config: T)

    abstract fun updateStatus(message: String)
}