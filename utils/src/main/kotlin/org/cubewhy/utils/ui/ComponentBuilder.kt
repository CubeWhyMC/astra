package org.cubewhy.utils.ui

import org.cubewhy.astra.events.Event
import org.cubewhy.astra.events.EventBus
import java.awt.*
import javax.swing.Box
import javax.swing.JComponent

/**
 * Abstract class that provides a builder pattern for creating UI components.
 *
 * This builder class allows constructing and configuring [Component] objects,
 * such as adding components to containers, setting layouts, handling events,
 * and applying other UI configurations like resizing and padding.
 *
 * @param T the type of component being built, which must be a subclass of [Component].
 */
abstract class ComponentBuilder<out T> where T : Component {
    /** The [Component] object being built by this builder. */
    abstract val component: T

    /** The index of the component, used when adding it to a container. */
    private var index: Int = -1

    /** The layout constraints for the component when adding it to a container. */
    private var constraints: Any? = null

    /**
     * Sets the index of the component when added to a container.
     *
     * @param index The index for the component in the container.
     */
    fun index(index: Int) {
        this.index = index
    }

    /**
     * Sets the layout constraints for the component when added to a container.
     *
     * @param constraints The layout constraints for this component.
     */
    fun constraints(constraints: Any?) {
        this.constraints = constraints
    }

    /**
     * Resizes the component to the specified width and height.
     *
     * @param width The desired width of the component.
     * @param height The desired height of the component.
     */
    fun resize(width: Int, height: Int) {
        this.component.size = Dimension(width, height)
    }

    /**
     * Adds another component to the current component (typically a container).
     *
     * @param builder The builder instance used to create the component to be added.
     */
    fun component(builder: ComponentBuilder<*>) {
        this.component(builder.build(), builder.constraints, builder.index)
    }

    /**
     * Adds a component to the current component (container).
     *
     * @param component The component to be added.
     * @param constraints The layout constraints for this component.
     * @param index The index at which to add the component in the container.
     */
    fun component(component: Component, constraints: Any? = null, index: Int = -1) {
        when (val container = this.component) {
            is Container -> container.add(component, constraints, index)
            else -> throw IllegalArgumentException("Unsupported container type: ${container::class.java}")
        }
    }

    /**
     * Adds vertical and/or horizontal padding to the component.
     *
     * This method adds vertical and horizontal struts (spacers) to the component.
     * Vertical padding is created with a [Box.createVerticalStrut] and horizontal padding
     * with a [Box.createHorizontalStrut].
     *
     * @param vertical The vertical padding size in pixels.
     * @param horizontal The horizontal padding size in pixels.
     */
    fun padding(vertical: Int = 0, horizontal: Int = 0) {
        if (vertical > 0) {
            this.component(Box.createVerticalStrut(vertical))
        }
        if (horizontal > 0) {
            this.component(Box.createHorizontalStrut(horizontal))
        }
    }

    /**
     * Registers an event handler for a specific event type.
     *
     * This method allows components to respond to events by registering a handler function.
     * The handler will be executed asynchronously when the event is triggered.
     *
     * @param E The event type that the handler will respond to.
     * @param handler The suspend function that will handle the event.
     */
    inline fun <reified E : Event> handleEvent(crossinline handler: suspend (E) -> Unit) {
        // Register the handler with the event bus
        EventBus.register(E::class) {
            handler(it as E)
        }
    }

    /**
     * Sets the layout for the component using a provided layout configuration function.
     *
     * @param init The lambda function that configures the layout manager for the component.
     * @return The created layout manager instance.
     */
    fun <L : LayoutManager> layout(init: T.() -> L): L {
        val layout = init.invoke(this.component)
        if (this.component is JComponent) {
            (this.component as JComponent).layout = layout
        }
        return layout
    }

    /**
     * Builds and returns the component created by this builder.
     *
     * This method must be implemented in concrete subclasses of [ComponentBuilder].
     *
     * @return The [Component] object that was constructed by the builder.
     */
    abstract fun build(): Component
}

/**
 * Extension function to set the background color of the component.
 *
 * @param color The background color to set.
 */
fun ComponentBuilder<JComponent>.background(color: Color) {
    this.component.background = color
}

/**
 * Extension function to set the foreground color of the component.
 *
 * @param color The foreground color to set.
 */
fun ComponentBuilder<JComponent>.foreground(color: Color) {
    this.component.foreground = color
}

/**
 * Extension function to set the alignment of the component along the X axis.
 *
 * @param alignmentX The alignment value along the X axis (0.0 to 1.0).
 */
fun ComponentBuilder<JComponent>.alignmentX(alignmentX: Float) {
    this.component.alignmentX = alignmentX
}

/**
 * Extension function to set the alignment of the component along the Y axis.
 *
 * @param alignmentY The alignment value along the Y axis (0.0 to 1.0).
 */
fun ComponentBuilder<JComponent>.alignmentY(alignmentY: Float) {
    this.component.alignmentY = alignmentY
}
