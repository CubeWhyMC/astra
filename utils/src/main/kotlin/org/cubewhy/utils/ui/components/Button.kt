package org.cubewhy.utils.ui.components

import org.cubewhy.utils.ui.ComponentBuilder
import org.cubewhy.utils.ui.ObservableState
import java.awt.Component
import javax.swing.JButton

class Button : ComponentBuilder<JButton>() {
    override val component: JButton = JButton()

    private var trigger: ObservableState<String>? = null

    fun text(string: String) {
        component.text = string
    }

    fun state(state: ObservableState<String>) {
        this.trigger = state
        // update text
        text(state.get())
    }

    fun onClick(action: () -> Unit) {
        component.addActionListener { action.invoke() }
    }

    override fun build(): Component {
        trigger?.observe { component.text = it }
        return component
    }
}

fun ComponentBuilder<*>.button(init: Button.() -> Unit) {
    this.component(Button().apply(init))
}
