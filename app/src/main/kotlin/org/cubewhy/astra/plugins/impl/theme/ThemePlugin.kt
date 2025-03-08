package org.cubewhy.astra.plugins.impl.theme

import com.formdev.flatlaf.FlatDarculaLaf
import com.formdev.flatlaf.FlatDarkLaf
import com.formdev.flatlaf.FlatIntelliJLaf
import com.formdev.flatlaf.FlatLightLaf
import com.formdev.flatlaf.themes.FlatMacDarkLaf
import com.formdev.flatlaf.themes.FlatMacLightLaf
import io.github.oshai.kotlinlogging.KotlinLogging
import org.cubewhy.astra.plugins.Plugin
import org.cubewhy.astra.plugins.annotations.PreInit
import org.cubewhy.astra.plugins.annotations.Scan
import org.cubewhy.astra.plugins.annotations.Unload
import org.cubewhy.astra.plugins.impl.theme.configs.ThemeConfig
import org.cubewhy.utils.ui.alignmentX
import org.cubewhy.utils.ui.boxLayout
import org.cubewhy.utils.ui.components.comboBox
import org.cubewhy.utils.ui.components.panel
import java.awt.Component
import javax.swing.BoxLayout
import javax.swing.UIManager

@Scan
class ThemePlugin : Plugin() {
    override val name: String = "Flatlaf Themes"
    override val version: String = "1.0.0"
    override val description: String = "Flatlaf themes for Astra Launcher"

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
            "dark" -> FlatDarkLaf()
            "darcula" -> FlatDarculaLaf()
            "mac-dark" -> FlatMacDarkLaf()
            "mac-light" -> FlatMacLightLaf()
            "intellij-light" -> FlatIntelliJLaf()
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

    override fun configPage() = panel {
        layout { boxLayout(BoxLayout.Y_AXIS) }
        alignmentX(Component.LEFT_ALIGNMENT)

        comboBox {
            alignmentX(Component.LEFT_ALIGNMENT)

            addOption("dark")
            addOption("mac-dark")
            addOption("darcula")
            addOption("intellij-light")
            addOption("light")
            addOption("mac-light")
            addOption("unset")

            select(config.theme)

            onChange { element ->
                config.theme = element
            }
        }
    }.build()
}