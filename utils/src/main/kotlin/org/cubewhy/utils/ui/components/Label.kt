package org.cubewhy.utils.ui.components

import org.cubewhy.utils.ui.ComponentBuilder
import org.cubewhy.utils.ui.ObservableState
import java.awt.Component
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
        component.text = text
    }

    override fun build(): Component {
        trigger?.observe { component.text = it }
        return component
    }
}

fun ComponentBuilder<*>.label(init: Label.() -> Unit = {}) {
    this.component(Label().apply(init))
}