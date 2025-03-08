package org.cubewhy.utils.ui

import java.awt.Component
import java.awt.Container
import java.awt.Dimension
import java.awt.LayoutManager
import javax.swing.Box
import javax.swing.JComponent

abstract class ComponentBuilder<out T> where T : Component {
    abstract val component: T
    private var index: Int = -1
    private var constraints: Any? = null

    fun index(index: Int) {
        this.index = index
    }

    fun constraints(constraints: Any?) {
        this.constraints = constraints
    }

    fun resize(width: Int, height: Int) {
        this.component.size = Dimension(width, height)
    }

    fun component(builder: ComponentBuilder<*>) {
        this.component(builder.build(), builder.constraints, builder.index)
    }

    fun component(component: Component, constraints: Any? = null, index: Int = -1) {
        when (val container = this.component) {
            is Container -> container.add(component, constraints, index)
            else -> throw IllegalArgumentException("Unsupported container type: ${container::class.java}")
        }
    }

    fun padding(vertical: Int = 0, horizontal: Int = 0) {
        if (vertical > 0) {
            this.component(Box.createVerticalStrut(vertical))
        }
        if (horizontal > 0) {
            this.component(Box.createHorizontalStrut(horizontal))
        }
    }

    fun <L : LayoutManager> layout(init: T.() -> L): L {
        val layout = init.invoke(this.component)
        if (this.component is JComponent) {
            (this.component as JComponent).layout = layout
        }
        return layout
    }

    abstract fun build(): Component
}