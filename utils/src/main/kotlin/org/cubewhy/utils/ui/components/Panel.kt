package org.cubewhy.utils.ui.components

import org.cubewhy.utils.ui.ComponentBuilder
import java.awt.Component
import javax.swing.JPanel

class Panel : ComponentBuilder<JPanel>() {
    override val component: JPanel = JPanel()

    override fun build(): Component {
        return component
    }
}

fun ComponentBuilder<*>.panel(init: Panel.() -> Unit) {
    val panel = Panel().apply(init)
    this.nativeComponent(panel.build())
}