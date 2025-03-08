package org.cubewhy.astra.pages.impl

import org.cubewhy.astra.plugins.Page
import org.cubewhy.utils.ui.boxLayout
import org.cubewhy.utils.ui.components.label
import org.cubewhy.utils.ui.components.panel
import javax.swing.BoxLayout

class WelcomePage : Page {
    override val name: String = "Welcome"

    override fun component() = panel {
        layout { boxLayout(BoxLayout.Y_AXIS) }
        label { text("Welcome to Astra Launcher") }
    }.build()
}