package org.cubewhy.utils.ui.components

import org.cubewhy.utils.ui.ComponentBuilder
import java.awt.Component
import java.awt.Container
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.WindowConstants

class Frame(
    title: String?,
    wight: Int, height: Int,
    defaultCloseOperation: Int,
) : ComponentBuilder<JFrame>() {
    override val component: JFrame = JFrame()

    init {
        component.title = title
        component.size = Dimension(wight, height)
        component.defaultCloseOperation = defaultCloseOperation
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
    title: String? = "Swing Application",
    wight: Int = 600, height: Int = 400,
    defaultCloseOperation: Int = WindowConstants.EXIT_ON_CLOSE,
    init: Frame.() -> Unit
) = Frame(
    title = title,
    wight = wight,
    height = height,
    defaultCloseOperation = defaultCloseOperation
).apply(init)