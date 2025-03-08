package org.cubewhy.utils.ui

import java.awt.BorderLayout
import java.awt.CardLayout
import java.awt.Container
import java.awt.FlowLayout
import java.awt.GridBagLayout
import java.awt.GridLayout
import javax.swing.BoxLayout
import javax.swing.GroupLayout

fun Container.boxLayout(axis: Int) = BoxLayout(this, axis)

fun flowLayout(alignment: Int = FlowLayout.CENTER, hgap: Int = 5, vgap: Int = 5) =
    FlowLayout(alignment, hgap, vgap)

fun borderLayout(hgap: Int = 0, vgap: Int = 0) =
    BorderLayout(hgap, vgap)

fun gridLayout(rows: Int, cols: Int, hgap: Int = 0, vgap: Int = 0) =
    GridLayout(rows, cols, hgap, vgap)

fun gridBagLayout() =
    GridBagLayout()

fun cardLayout(hgap: Int = 0, vgap: Int = 0) =
    CardLayout(hgap, vgap)

fun Container.groupLayout() =
    GroupLayout(this)
