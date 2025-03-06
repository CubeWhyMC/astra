package org.cubewhy.utils.ui

import java.awt.Component
import java.awt.LayoutManager
import javax.swing.JComponent
import javax.swing.JFrame

abstract class ComponentBuilder<out T> where T : Component {
    abstract val component: T

    fun nativeComponent(component: Component) {
        if (this.component is JComponent) {
            (this.component as JComponent).add(component)
        } else if (this.component is JFrame) {
            (this.component as JFrame).add(component)
        }
    }

    fun layout(init: T.() -> LayoutManager) {
        val layout = init.invoke(this.component)
        if (this.component is JComponent) {
            (this.component as JComponent).layout = layout
        }
    }

    abstract fun build(): Component
}