package org.cubewhy.astra.pages.impl

import org.cubewhy.astra.plugins.Page
import org.cubewhy.astra.ui.t
import org.cubewhy.utils.ui.alignmentX
import org.cubewhy.utils.ui.border
import org.cubewhy.utils.ui.boxLayout
import org.cubewhy.utils.ui.components.label
import org.cubewhy.utils.ui.components.panel
import org.cubewhy.utils.ui.flowLayout
import java.awt.Component
import java.awt.FlowLayout
import javax.swing.BorderFactory
import javax.swing.BoxLayout

class WelcomePage : Page {
    override val name: String = t("gui.page.welcome.name")

    override fun component() = panel {
        layout { flowLayout(FlowLayout.CENTER) }

        panel {
            layout { boxLayout(BoxLayout.Y_AXIS) }
            alignmentX(Component.CENTER_ALIGNMENT)
            border { BorderFactory.createTitledBorder(name) }

            label {
                size(30)
                text(t("gui.page.welcome.title"))
            }
            label {
                size(15)
                text(t("gui.page.welcome.tip"))
            }
        }
    }.build()
}