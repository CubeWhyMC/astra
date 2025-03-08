package org.cubewhy.utils.ui.components

import org.cubewhy.utils.ui.ComponentBuilder
import java.awt.Component
import javax.swing.JScrollPane
import javax.swing.border.Border

class ScrollPane : ComponentBuilder<JScrollPane>() {
    override val component: JScrollPane = JScrollPane()

    fun horizontalScrollBarPolicy(policy: Int) {
        if (policy == JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {
            this.component.horizontalScrollBar = null
            return
        }
        this.component.horizontalScrollBarPolicy = policy
    }

    fun verticalScrollBarPolicy(policy: Int) {
        this.component.verticalScrollBarPolicy = policy
    }

    fun viewPortView(init: Panel.() -> Unit) {
        val panel = Panel().apply(init)
        this.component.setViewportView(panel.build())
    }

    fun border(init: ComponentBuilder<JScrollPane>.() -> Border) {
        this.component.border = init.invoke(this)
    }

    override fun build(): Component {
        return component
    }
}

fun ComponentBuilder<*>.scrollPane(init: ScrollPane.() -> Unit) {
    val scrollPane = ScrollPane().apply(init)
    this.component(scrollPane)
}