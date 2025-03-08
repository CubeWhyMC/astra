package org.cubewhy.astra.plugins.impl

import com.formdev.flatlaf.FlatDarculaLaf
import io.github.oshai.kotlinlogging.KotlinLogging
import org.cubewhy.astra.plugins.Plugin
import org.cubewhy.astra.plugins.annotations.PreInit
import org.cubewhy.astra.plugins.annotations.Scan
import javax.swing.UIManager

@Scan
class ThemePlugin : Plugin {
    override val name: String = "Themes"

    companion object {
        private val logger = KotlinLogging.logger {}
    }

    @PreInit
    fun preInit() {
        logger.info { "Init themes" }
        UIManager.setLookAndFeel(FlatDarculaLaf())
    }
}