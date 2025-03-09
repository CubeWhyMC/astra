package org.cubewhy.astra.events

import org.cubewhy.astra.plugins.Plugin
import org.cubewhy.astra.plugins.PluginState

class PostInitEvent: Event

class TogglePluginEvent(val plugin: Plugin, val state: PluginState) : Event

class UpdateStatusEvent(val text: String) : Event
