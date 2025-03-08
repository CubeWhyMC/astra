package org.cubewhy.astra.pages.impl

import org.cubewhy.astra.events.TogglePluginEvent
import org.cubewhy.astra.plugins.InternalPlugin
import org.cubewhy.astra.plugins.Page
import org.cubewhy.astra.plugins.PluginManager
import org.cubewhy.astra.plugins.PluginState
import org.cubewhy.utils.ui.*
import org.cubewhy.utils.ui.components.button
import org.cubewhy.utils.ui.components.label
import org.cubewhy.utils.ui.components.list
import org.cubewhy.utils.ui.components.panel
import java.awt.BorderLayout
import java.awt.Component
import java.awt.FlowLayout
import javax.swing.BorderFactory
import javax.swing.BoxLayout

class PluginsPage : Page {
    override val name: String
        get() = "Plugins"

    override fun component() =
        panel {
            layout { borderLayout() }
            border { BorderFactory.createTitledBorder("Plugins") }

            val selectedPlugin = observableStateOf<InternalPlugin?>(null)

            list {
                constraints(BorderLayout.WEST)

                cellRenderer { list, value: InternalPlugin, _, isSelected, _ ->
                    layout { flowLayout(FlowLayout.LEFT) }

                    if (isSelected) {
                        background(list.selectionBackground)
                        foreground(list.selectionForeground)
                    } else {
                        background(list.background)
                        foreground(list.foreground)
                    }

                    label { text(value.instance.name) }
                }

                onChange { plugin ->
                    selectedPlugin.set(plugin)
                }

                listen(PluginManager.plugins)
            }

            panel {
                val layout = layout { cardLayout() }
                constraints(BorderLayout.CENTER)

                panel {
                    constraints("welcome")

                    label {
                        text("Click a plugin to start")
                    }
                }

                // config panels
                PluginManager.plugins.forEach { plugin ->
                    pluginConfigPanel(plugin)
                }

                PluginManager.plugins.addListener { changeType, plugin, index ->
                    if (changeType == ObservableList.ChangeType.ADD) {
                        pluginConfigPanel(plugin!!)
                    }
                }


                selectedPlugin.observe {
                    if (it == null) {
                        layout.show(component, "welcome")
                    } else {
                        layout.show(component, "cfg:${it.instance::class.java.name}")
                    }
                }
            }
        }.build()
}

private fun ComponentBuilder<*>.pluginConfigPanel(plugin: InternalPlugin) {
    panel {
        constraints("cfg:${plugin.instance::class.java.name}")
        layout { borderLayout() }
        border { BorderFactory.createTitledBorder("${plugin.instance.name} - ${plugin.instance.version}") }

        panel {
            constraints(BorderLayout.NORTH)
            border { BorderFactory.createTitledBorder("Plugin Information") }
            layout { boxLayout(BoxLayout.Y_AXIS) }

            plugin.instance.description?.let { description ->
                label {
                    alignmentX(Component.LEFT_ALIGNMENT)
                    text(description)
                }
            }

            label { text("Plugin class: ${plugin.instance::class.java.name}") }

            val toggleButtonText = observableStateOf("Something went wrong, please restart")

            padding(vertical = 5)
            button {
                state(toggleButtonText)

                onClick {
                    if (plugin.state == PluginState.ENABLED) {
                        PluginManager.disablePlugin(plugin.instance)
                    } else {
                        PluginManager.enablePlugin(plugin.instance)
                    }
                }

                handleEvent { e: TogglePluginEvent ->
                    if (e.plugin == plugin.instance) {
                        toggleButtonText.set(
                            when (e.state) {
                                PluginState.ENABLED -> "Disable Plugin"
                                PluginState.DISABLED -> "Enable Plugin"
                            }
                        )
                    }
                }
            }
        }

        if (plugin.state == PluginState.ENABLED) {
            plugin.instance.configPage()?.let { configPage ->
                panel {
                    border { BorderFactory.createTitledBorder("Plugin Configuration") }
                    component(configPage)
                }
            }
        }
    }
}