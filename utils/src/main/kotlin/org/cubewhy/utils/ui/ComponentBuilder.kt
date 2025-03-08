package org.cubewhy.utils.ui

import org.cubewhy.astra.events.Event
import org.cubewhy.astra.events.EventBus
import java.awt.*
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

    inline fun <reified E: Event> handleEvent(crossinline handler: suspend (E) -> Unit) {
        // register handler
        EventBus.register(E::class) {
            handler(it as E)
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

fun ComponentBuilder<JComponent>.background(color: Color) {
    this.component.background = color
}

fun ComponentBuilder<JComponent>.foreground(color: Color) {
    this.component.foreground = color
}

fun ComponentBuilder<JComponent>.alignmentX(alignmentX: Float) {
    this.component.alignmentX = alignmentX
}

fun ComponentBuilder<JComponent>.alignmentY(alignmentY: Float) {
    this.component.alignmentY = alignmentY
}