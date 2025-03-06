package org.cubewhy.utils.ui.components

import org.cubewhy.utils.ui.ComponentBuilder
import java.awt.Component
import java.awt.Container
import java.awt.Dimension
import javax.swing.JFrame

class Frame : ComponentBuilder<JFrame>() {
    override val component: JFrame = JFrame()

    fun title(title: String) {
        this.component.title = title
    }

    fun size(width: Int, height: Int) {
        this.component.size = Dimension(width, height)
    }

    fun defaultCloseOperation(operation: Int) {
        this.component.defaultCloseOperation = operation
    }

    fun contentPane(init: Panel.() -> Unit) {
        val panelComponent = Panel().apply(init)
        this.component.contentPane = panelComponent.build() as Container?
    }

    override fun build(): Component {
        return component
    }
}

fun frame(
    init: Frame.() -> Unit
) = Frame().apply(init)