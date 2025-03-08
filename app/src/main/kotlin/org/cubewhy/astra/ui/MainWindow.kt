package org.cubewhy.astra.ui

import org.cubewhy.astra.pages.PageManager
import org.cubewhy.utils.ui.ComponentBuilder
import org.cubewhy.utils.ui.ObservableState
import org.cubewhy.utils.ui.cardLayout
import org.cubewhy.utils.ui.components.panel

fun ComponentBuilder<*>.mainWindow(
    currentPage: ObservableState<String>
) {
    panel {
        val layout = layout { cardLayout() }

        currentPage.observe { page ->
            // do navigate
            layout.show(component, page)
        }

        // load pages
        PageManager.internalPages.forEach { page ->
            component(page.component(), page::class.java.name)
        }
        PageManager.externalPages.forEach { page ->
            component(page.component(), page::class.java.name)
        }
    }
}