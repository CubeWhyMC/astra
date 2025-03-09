package org.cubewhy.astra

import io.github.oshai.kotlinlogging.KotlinLogging
import org.cubewhy.astra.configs.ConfigManager
import org.cubewhy.astra.events.EventBus
import org.cubewhy.astra.events.PostInitEvent
import org.cubewhy.astra.pages.PageManager
import org.cubewhy.astra.pages.impl.PluginsPage
import org.cubewhy.astra.pages.impl.SettingsPage
import org.cubewhy.astra.pages.impl.WelcomePage
import org.cubewhy.astra.plugins.Plugin
import org.cubewhy.astra.plugins.PluginManager
import org.cubewhy.astra.ui.*
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


    // load config
    ConfigManager.load()

    // init language
    Translate.useLanguage(ConfigManager.config.language)

    // todo load plugins from jar
    // register plugins
    ClassScanner.scanRegisteredClasses().forEach { clazz ->
        if (clazz.java.superclass == Plugin::class.java) {
            @Suppress("UNCHECKED_CAST") val pluginClass = clazz.java as Class<out Plugin>
            if (!ConfigManager.config.disabledPlugins.contains(clazz.java.name)) {
                PluginManager.registerPlugin(pluginClass)
            } else {
                PluginManager.registerDisabledPlugin(pluginClass)
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

    // load pages
    PageManager.registerInternalPage(WelcomePage())
    PageManager.registerInternalPage(PluginsPage())
    PageManager.registerInternalPage(SettingsPage())

    val window = frame {
        title(t("gui.title"))
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
            // unload plugins
            PluginManager.unloadPlugins()
            // save config
            ConfigManager.save()
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