package org.cubewhy.astra.plugins

abstract class AstraBridge {
    abstract fun <T> loadConfig(defaltValue: T): T
    abstract fun <T> saveConfig(config: T)
}