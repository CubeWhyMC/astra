package org.cubewhy.astra.plugins.impl.theme

import com.formdev.flatlaf.FlatDarculaLaf
import com.formdev.flatlaf.FlatLightLaf
import io.github.oshai.kotlinlogging.KotlinLogging
import org.cubewhy.astra.plugins.Plugin
import org.cubewhy.astra.plugins.annotations.PreInit
import org.cubewhy.astra.plugins.annotations.Scan
import org.cubewhy.astra.plugins.annotations.Unload
import org.cubewhy.astra.plugins.impl.theme.configs.ThemeConfig
import javax.swing.UIManager

@Scan
class ThemePlugin : Plugin() {
    override val name: String = "Themes"

    companion object {
        private val logger = KotlinLogging.logger {}
        private lateinit var config: ThemeConfig
    }

    @PreInit
    fun preInit() {
        // load config
        config = bridge.loadConfig(ThemeConfig())
        logger.info { "Use theme ${config.theme}" }
        // load theme
        val theme = when (config.theme) {
            "dark" -> FlatDarculaLaf()
            "light" -> FlatLightLaf()
            "unset" -> null
            else -> throw IllegalArgumentException("Unknown theme ${config.theme}") // todo custom theme
        }
        theme?.let {
            UIManager.setLookAndFeel(it)
        }
    }

    @Unload
    fun unload() {
        // save config
        bridge.saveConfig(config)
    }
}