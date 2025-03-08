package org.cubewhy.utils.ui.components

import org.cubewhy.utils.ui.ComponentBuilder
import org.cubewhy.utils.ui.ObservableList
import java.awt.Component
import javax.swing.DefaultListModel
import javax.swing.JList
import javax.swing.ListCellRenderer

class List<T> : ComponentBuilder<JList<T>>() {
    private val model = DefaultListModel<T>()
    override val component: JList<T> = JList<T>(model)

    fun cellRenderer(init: Panel.(list: JList<out T>, value: T, index: Int, isSelected: Boolean, cellHasFocus: Boolean) -> Unit) {
        val renderer =
            ListCellRenderer { list: JList<out T>, value: T, index: Int, isSelected: Boolean, cellHasFocus: Boolean ->
                // create panel
                Panel().apply {
                    init(this, list, value, index, isSelected, cellHasFocus)
                }.build()
            }

        // apply cellRenderer
        this.component.cellRenderer = renderer
    }

    fun addAll(list: kotlin.collections.List<T>) {
        this.model.addAll(list)
    }

    fun onChange(callback: (T?) -> Unit) {
        this.component.addListSelectionListener {
            callback.invoke(this.component.selectedValue)
        }
    }

    fun listen(list: ObservableList<out T>) {
        this.model.addAll(list)

        // listen to changes
        list.addListener { changeType, element, index ->
            when (changeType) {
                ObservableList.ChangeType.ADD -> this.model.add(index, element)
                ObservableList.ChangeType.REMOVE -> this.model.removeElement(element)
                ObservableList.ChangeType.UPDATE -> {
                    if (index == -1) {
                        this.model.removeAllElements()
                        this.model.addAll(list)
                    } else {
                        this.model.set(index, element)
                    }
                }
                ObservableList.ChangeType.CLEAR -> this.model.removeAllElements()
            }
        }
    }

    override fun build(): Component {
        return component
    }
}

fun <T> ComponentBuilder<*>.list(init: List<T>.() -> Unit) {
    val list = List<T>().apply(init)
    this.component(list)
}