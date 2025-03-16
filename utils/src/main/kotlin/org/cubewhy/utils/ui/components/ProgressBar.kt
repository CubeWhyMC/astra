package org.cubewhy.utils.ui.components

import org.cubewhy.utils.ui.ComponentBuilder
import java.awt.Component
import javax.swing.JProgressBar

/**
 * A DSL builder for creating and configuring a [JProgressBar].
 *
 * This builder provides a declarative way to set up a progress bar with common properties.
 */
class ProgressBar : ComponentBuilder<JProgressBar>() {
    override val component: JProgressBar = JProgressBar()

    /**
     * Sets the minimum value of the progress bar.
     *
     * @param min The minimum value (default is 0).
     */
    fun minimum(min: Int) {
        component.minimum = min
    }

    /**
     * Sets the maximum value of the progress bar.
     *
     * @param max The maximum value (default is 100).
     */
    fun maximum(max: Int) {
        component.maximum = max
    }

    /**
     * Sets the current value of the progress bar.
     *
     * @param value The current progress value.
     */
    fun value(value: Int) {
        component.value = value
    }

    /**
     * Sets whether the progress bar is in indeterminate mode.
     *
     * @param isIndeterminate `true` to enable indeterminate mode (for unknown progress),
     *                       `false` to disable it.
     */
    fun indeterminate(isIndeterminate: Boolean) {
        component.isIndeterminate = isIndeterminate
    }

    /**
     * Sets the string to be displayed on the progress bar.
     *
     * @param string The string to display (e.g., "Loading...").
     */
    fun string(string: String) {
        component.string = string
    }

    /**
     * Sets whether the progress bar should display a string.
     *
     * @param isPainted `true` to display the string, `false` to hide it.
     */
    fun stringPainted(isPainted: Boolean) {
        component.isStringPainted = isPainted
    }

    /**
     * Sets the orientation of the progress bar.
     *
     * @param orientation The orientation of the progress bar.
     *                    Use [JProgressBar.HORIZONTAL] or [JProgressBar.VERTICAL].
     */
    fun orientation(orientation: Int) {
        component.orientation = orientation
    }

    override fun build(): Component {
        return component
    }
}

/**
 * Creates a [JProgressBar] and applies the provided configuration.
 *
 * @param init A lambda to configure the [ProgressBar] instance.
 */
fun ComponentBuilder<*>.progressBar(
    init: ProgressBar.() -> Unit
) {
    val pg = ProgressBar().apply(init)
    this.component(pg)
}