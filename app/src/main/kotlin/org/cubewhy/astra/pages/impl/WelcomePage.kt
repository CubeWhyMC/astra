package org.cubewhy.astra.pages.impl

import org.cubewhy.astra.plugins.Page
import org.cubewhy.utils.ui.alignmentX
import org.cubewhy.utils.ui.boxLayout
import org.cubewhy.utils.ui.components.label
import org.cubewhy.utils.ui.components.panel
import org.cubewhy.utils.ui.flowLayout
import java.awt.Component
import java.awt.FlowLayout
import javax.swing.BoxLayout

class WelcomePage : Page {
    override val name: String = "Welcome"

    override fun component() = panel {
        layout { flowLayout(FlowLayout.CENTER) }

        panel {
            layout { boxLayout(BoxLayout.Y_AXIS) }
            alignmentX(Component.CENTER_ALIGNMENT)

            label {
                size(30)
                text("Welcome to Astra Launcher")
            }
            label {
                size(15)
                text("Add plugins to getting started.")
            }
        }
    }.build()
}