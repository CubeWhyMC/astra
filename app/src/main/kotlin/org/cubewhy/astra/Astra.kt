package org.cubewhy.astra

import com.formdev.flatlaf.FlatDarculaLaf
import io.github.oshai.kotlinlogging.KotlinLogging
import org.cubewhy.utils.ui.components.frame
import javax.swing.BoxLayout
import javax.swing.SwingUtilities
import javax.swing.UIManager
import javax.swing.WindowConstants


private val logger = KotlinLogging.logger {}

fun main() {
    logger.info { "Welcome to Astra!" }
    logger.info { "Powered by LunarCN https://lunarclient.top" }
    try {
        logger.info { "Init flatlaf" }
        UIManager.setLookAndFeel(FlatDarculaLaf())
    } catch (ex: Exception) {
        logger.error(ex) { "Error initializing FlatDarculaLaf" }
    }
    val window = frame {
        title("Astra Launcher")
        size(950, 600)
        defaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)

        contentPane {
            layout { BoxLayout(this, BoxLayout.Y_AXIS) }
            // todo gui
        }
    }

    SwingUtilities.invokeLater {
        // build & display
        window.build().apply {
            isVisible = true
        }
    }
}