package org.cubewhy.astra

import io.github.oshai.kotlinlogging.KotlinLogging
import org.cubewhy.astra.configs.ConfigManager
import org.cubewhy.astra.events.EventBus
import org.cubewhy.astra.events.PostInitEvent
import org.cubewhy.astra.plugins.Plugin
import org.cubewhy.astra.plugins.PluginManager
import org.cubewhy.astra.ui.mainWindow
import org.cubewhy.astra.ui.navBar
import org.cubewhy.astra.ui.statusBar
import org.cubewhy.utils.ClassScanner
import org.cubewhy.utils.ui.borderLayout
import org.cubewhy.utils.ui.components.frame
import org.cubewhy.utils.ui.observableStateOf
import javax.swing.SwingUtilities
import javax.swing.WindowConstants


private val logger = KotlinLogging.logger {}

fun main() {
    logger.info { "Welcome to Astra!" }
    logger.info { "Powered by LunarCN https://lunarclient.top" }

    // todo load plugins from jar

    // load config
    ConfigManager.load()

    ClassScanner.scanRegisteredClasses().forEach { clazz ->
        if (clazz.java.superclass == Plugin::class.java) {
            if (!ConfigManager.config.disabledPlugins.contains(clazz.java.name)) {
                PluginManager.registerPlugin(clazz.java as Class<out Plugin>)
            }
        }
    }
    // load plugins
    PluginManager.loadPlugins()

    // init gui
    ui()
}

private fun ui() {
    logger.info { "Init GUI" }

    val window = frame {
        title("Astra Launcher")
        size(950, 600)
        defaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)

        contentPane {
            layout { borderLayout() }

            val currentPage = observableStateOf("welcome")

            navBar(currentPage)
            statusBar()
            mainWindow(currentPage)
        }

        onExit {
            // save config
            ConfigManager.save()
            // unload plugins
            PluginManager.unloadPlugins()
        }
    }

    SwingUtilities.invokeLater {
        // build & display
        window.build().apply {
            isVisible = true
        }
        // publish event
        EventBus.post(PostInitEvent())
    }
}