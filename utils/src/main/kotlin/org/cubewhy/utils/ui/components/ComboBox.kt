package org.cubewhy.utils.ui.components

import org.cubewhy.utils.ui.ComponentBuilder
import java.awt.Component
import javax.swing.DefaultComboBoxModel
import javax.swing.JComboBox

class ComboBox<E> : ComponentBuilder<JComboBox<E>>() {
    override val component: JComboBox<E> = JComboBox<E>()
    private val model = DefaultComboBoxModel<E>()

    init {
        component.model = model
    }

    fun addOption(element: E) {
        model.addElement(element)
    }

    fun insertOption(element: E, index: Int) {
        model.insertElementAt(element, index)
    }

    fun addOptions(elements: Collection<E>) {
        model.addAll(elements)
    }

    fun removeOption(element: E) {
        model.removeElement(element)
    }

    fun select(element: E) {
        model.selectedItem = element
    }

    fun select(index: Int) {
        model.selectedItem = model.getElementAt(index)
    }

    fun removeOption(index: Int) {
        model.removeElementAt(index)
    }

    fun removeAllOptions() {
        model.removeAllElements()
    }

    @Suppress("UNCHECKED_CAST")
    fun onChange(callback: (E) -> Unit) {
        component.addActionListener { callback(component.selectedItem as E) }
    }

    override fun build(): Component {
        return component
    }

}

fun <E> ComponentBuilder<*>.comboBox(init: ComboBox<E>.() -> Unit) {
    val comboBox = ComboBox<E>().apply(init)
    this.component(comboBox)
}