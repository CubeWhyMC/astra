package org.cubewhy.astra.pages.impl

import org.cubewhy.astra.configs.ConfigManager
import org.cubewhy.astra.configs.Language
import org.cubewhy.astra.plugins.Page
import org.cubewhy.astra.ui.t
import org.cubewhy.utils.ui.alignmentX
import org.cubewhy.utils.ui.border
import org.cubewhy.utils.ui.boxLayout
import org.cubewhy.utils.ui.components.comboBox
import org.cubewhy.utils.ui.components.label
import org.cubewhy.utils.ui.components.panel
import org.cubewhy.utils.ui.flowLayout
import java.awt.Component
import java.awt.FlowLayout
import javax.swing.BorderFactory
import javax.swing.BoxLayout

class SettingsPage : Page {
    override val name: String = t("gui.page.settings.name")

    override fun component() = panel {
        layout { boxLayout(BoxLayout.Y_AXIS) }
        border { BorderFactory.createTitledBorder(name) }
        alignmentX(Component.LEFT_ALIGNMENT)

        panel {
            layout { boxLayout(BoxLayout.Y_AXIS) }
            alignmentX(Component.LEFT_ALIGNMENT)
            border { BorderFactory.createTitledBorder(t("gui.page.settings.astra")) }

            panel {
                layout { flowLayout(FlowLayout.LEFT) }

                label { text(t("gui.page.settings.astra.language")) }
                comboBox {
                    addOptions(Language.entries)
                    select(ConfigManager.config.language)

                    onChange { newValue ->
                        ConfigManager.config.language = newValue
                    }
                }
            }
        }
    }.build()
}