package org.cubewhy.astra.pages

import org.cubewhy.astra.plugins.Page

object PageManager {
    private val externalPages0: MutableList<Page> = mutableListOf()
    private val internalPages0: MutableList<Page> = mutableListOf()

    val internalPages: List<Page>
        get() = internalPages0.toList()
    val externalPages: List<Page>
        get() = externalPages0.toList()

    internal fun registerInternalPage(page: Page) {
        internalPages0.add(page)
    }

    fun registerPage(page: Page) {
        externalPages0.add(page)
    }
}