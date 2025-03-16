package org.cubewhy.utils.ui.components

import org.cubewhy.utils.ui.ComponentBuilder
import org.cubewhy.utils.ui.ObservableState
import java.awt.Component
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.PlainDocument

/**
 * A DSL builder for creating and configuring a [JTextField].
 *
 * This builder provides a declarative way to set up a text field with common properties.
 */
class TextField : ComponentBuilder<JTextField>() {
    override val component: JTextField = JTextField()

    /**
     * Sets the text of the text field.
     *
     * @param text The text to display in the text field.
     */
    fun text(text: String) {
        component.text = text
    }

    fun state(state: ObservableState<String>) {
        this.text(state.get())
        state.observe { component.text = it }

        this.component.document.addDocumentListener(object: DocumentListener {
            override fun insertUpdate(e: DocumentEvent) {
                onTextChanged(e)
            }

            override fun removeUpdate(e: DocumentEvent) {
                onTextChanged(e)
            }

            override fun changedUpdate(e: DocumentEvent) {
                onTextChanged(e)
            }

            private fun onTextChanged(e: DocumentEvent) {
                val text = (e.document as PlainDocument).getText(0, e.document.length)
                state.setSilently(text)
            }
        })
    }

    /**
     * Sets the number of columns in the text field.
     *
     * @param columns The number of columns (width) of the text field.
     */
    fun columns(columns: Int) {
        component.columns = columns
    }

    /**
     * Sets whether the text field is editable.
     *
     * @param isEditable `true` to make the text field editable, `false` to make it read-only.
     */
    fun editable(isEditable: Boolean) {
        component.isEditable = isEditable
    }

    /**
     * Sets the tooltip text for the text field.
     *
     * @param tooltip The tooltip text to display when hovering over the text field.
     */
    fun tooltip(tooltip: String) {
        component.toolTipText = tooltip
    }

    /**
     * Sets the horizontal alignment of the text in the text field.
     *
     * @param alignment The horizontal alignment of the text.
     *                 Use [JTextField.LEFT], [JTextField.CENTER], or [JTextField.RIGHT].
     */
    fun horizontalAlignment(alignment: Int) {
        component.horizontalAlignment = alignment
    }

    override fun build(): Component {
        return component
    }
}

/**
 * Creates a [JTextField] and applies the provided configuration.
 *
 * @param init A lambda to configure the [TextField] instance.
 */
fun ComponentBuilder<*>.textField(
    init: TextField.() -> Unit
) {
    val textField = TextField().apply(init)
    this.component(textField)
}