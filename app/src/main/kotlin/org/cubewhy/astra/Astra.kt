package org.cubewhy.astra

import io.github.oshai.kotlinlogging.KotlinLogging
import org.cubewhy.utils.ui.components.frame
import org.cubewhy.utils.ui.components.label
import javax.swing.BoxLayout
import javax.swing.SwingUtilities

private val logger = KotlinLogging.logger {}

fun main() {
    val window = frame(title = "Astra Launcher", wight = 950, height = 600) {
        // add more components here
        contentPane {
            layout { BoxLayout(this, BoxLayout.Y_AXIS) }
            label("Astra Launcher")
        }

    }
    SwingUtilities.invokeLater {
        // display
        window.build().apply {
            isVisible = true
        }
    }
}