package org.cubewhy.astra.plugins

import java.awt.Component

interface Page {
    val name: String

    fun component(): Component
}
