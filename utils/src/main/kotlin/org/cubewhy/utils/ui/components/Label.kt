package org.cubewhy.utils.ui.components

import org.cubewhy.utils.ui.ComponentBuilder
import org.cubewhy.utils.ui.ObservableState
import java.awt.Component
import javax.swing.JLabel

class Label() : ComponentBuilder<JLabel>() {
    override val component: JLabel = JLabel()

    private var trigger: ObservableState<String>? = null

    constructor(state: ObservableState<String>) : this() {
        this.trigger = state
    }

    constructor(text: String) : this() {
        component.text = text
    }

    override fun build(): Component {
        trigger?.observe { component.text = it }
        return component
    }
}

fun ComponentBuilder<*>.label(state: ObservableState<String>, init: Label.() -> Unit = {}) {
    val label = Label(
        state = state,
    ).apply(init)
    this.nativeComponent(label.build())
}

fun ComponentBuilder<*>.label(text: String, init: Label.() -> Unit = {}) {
    val label = Label(
        text = text,
    ).apply(init)
    this.nativeComponent(label.build())
}