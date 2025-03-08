package org.cubewhy.utils.ui.components

import org.cubewhy.utils.ui.ComponentBuilder
import java.awt.Component
import javax.swing.JPanel
import javax.swing.border.Border

class Panel : ComponentBuilder<JPanel>() {
    override val component: JPanel = JPanel()

    fun border(init: ComponentBuilder<JPanel>.() -> Border) {
        this.component.border = init.invoke(this)
    }

    override fun build(): Component {
        return component
    }
}

fun panel(init: Panel.() -> Unit) = Panel().apply(init)

fun ComponentBuilder<*>.panel(init: Panel.() -> Unit) {
    val panel = Panel().apply(init)
    this.component(panel)
}