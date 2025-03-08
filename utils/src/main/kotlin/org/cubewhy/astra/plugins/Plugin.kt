package org.cubewhy.astra.plugins

abstract class Plugin {
    abstract val name: String
    lateinit var bridge: AstraBridge
}