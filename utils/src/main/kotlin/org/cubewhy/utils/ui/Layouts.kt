package org.cubewhy.utils.ui

import java.awt.*
import javax.swing.BoxLayout
import javax.swing.GroupLayout

/**
 * Sets a [BoxLayout] on the current container with the specified axis.
 *
 * BoxLayout arranges components either vertically or horizontally. The axis determines
 * whether the layout is applied vertically (BoxLayout.Y_AXIS) or horizontally (BoxLayout.X_AXIS).
 *
 * @param axis The axis on which the components should be laid out. It can be either
 *             [BoxLayout.X_AXIS] or [BoxLayout.Y_AXIS].
 * @return The configured [BoxLayout] instance.
 */
fun Container.boxLayout(axis: Int) = BoxLayout(this, axis)

/**
 * Creates a [FlowLayout] with the specified alignment and gap values.
 *
 * The [FlowLayout] is a simple layout that arranges components in a left-to-right flow.
 * Components wrap to the next line when they exceed the container's width.
 *
 * @param alignment The alignment of components, can be one of:
 *                  [FlowLayout.LEFT], [FlowLayout.CENTER], or [FlowLayout.RIGHT].
 * @param hgap The horizontal gap between components.
 * @param vgap The vertical gap between components.
 * @return The configured [FlowLayout] instance.
 */
fun flowLayout(alignment: Int = FlowLayout.CENTER, hgap: Int = 5, vgap: Int = 5) =
    FlowLayout(alignment, hgap, vgap)

/**
 * Creates a [BorderLayout] with the specified horizontal and vertical gaps.
 *
 * [BorderLayout] arranges components in five regions: North, South, East, West, and Center.
 * This layout divides the container into five sections.
 *
 * @param hgap The horizontal gap between the components.
 * @param vgap The vertical gap between the components.
 * @return The configured [BorderLayout] instance.
 */
fun borderLayout(hgap: Int = 0, vgap: Int = 0) =
    BorderLayout(hgap, vgap)

/**
 * Creates a [GridLayout] with the specified number of rows and columns.
 *
 * [GridLayout] arranges components in a grid with the specified number of rows and columns.
 * All cells in the grid are of equal size.
 *
 * @param rows The number of rows in the grid.
 * @param cols The number of columns in the grid.
 * @param hgap The horizontal gap between cells.
 * @param vgap The vertical gap between cells.
 * @return The configured [GridLayout] instance.
 */
fun gridLayout(rows: Int, cols: Int, hgap: Int = 0, vgap: Int = 0) =
    GridLayout(rows, cols, hgap, vgap)

/**
 * Creates a [GridBagLayout] instance.
 *
 * [GridBagLayout] arranges components in a grid of cells, where components can span
 * multiple rows or columns, and their positions are determined by a set of constraints.
 *
 * @return The configured [GridBagLayout] instance.
 */
fun gridBagLayout() =
    GridBagLayout()

/**
 * Creates a [CardLayout] with the specified horizontal and vertical gaps.
 *
 * [CardLayout] manages a collection of components as a stack of "cards", where only
 * one card is visible at a time. You can navigate between the cards using methods
 * like [CardLayout.next] or [CardLayout.previous].
 *
 * @param hgap The horizontal gap between the cards.
 * @param vgap The vertical gap between the cards.
 * @return The configured [CardLayout] instance.
 */
fun cardLayout(hgap: Int = 0, vgap: Int = 0) =
    CardLayout(hgap, vgap)

/**
 * Sets a [GroupLayout] on the current container.
 *
 * [GroupLayout] is a layout manager that arranges components in horizontal and vertical groups.
 * It allows you to define how components are aligned, and their relative positions in the container.
 *
 * @return The configured [GroupLayout] instance.
 */
fun Container.groupLayout() =
    GroupLayout(this)
