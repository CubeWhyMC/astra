package org.cubewhy.utils.ui.components

import org.cubewhy.utils.ui.ComponentBuilder
import org.cubewhy.utils.ui.ObservableState
import java.awt.Component
import java.awt.Font
import javax.swing.JLabel

class Label : ComponentBuilder<JLabel>() {
    override val component: JLabel = JLabel()

    private var trigger: ObservableState<String>? = null

    fun state(state: ObservableState<String>) {
        this.trigger = state
        // update text
        text(state.get())
    }

    fun text(text: String) {
        this.component.text = text
    }

    fun size(size: Int) {
        this.component.font = Font(this.component.font.name, this.component.font.style, size)
    }

    override fun build(): Component {
        trigger?.observe { this.component.text = it }
        return this.component
    }
}

fun ComponentBuilder<*>.label(init: Label.() -> Unit = {}) {
    this.component(Label().apply(init))
}