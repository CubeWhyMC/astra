package org.cubewhy.astra.ui

import org.cubewhy.utils.ui.ComponentBuilder
import org.cubewhy.utils.ui.borderLayout
import org.cubewhy.utils.ui.components.label
import org.cubewhy.utils.ui.components.panel
import java.awt.BorderLayout

fun ComponentBuilder<*>.statusBar() {
    panel {
        layout { borderLayout() }
        constraints(BorderLayout.SOUTH)
        label { text("status bar") }
    }
}