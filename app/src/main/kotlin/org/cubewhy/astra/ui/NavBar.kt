package org.cubewhy.astra.ui

import org.cubewhy.astra.plugins.Page
import org.cubewhy.astra.pages.PageManager
import org.cubewhy.utils.ui.*
import org.cubewhy.utils.ui.components.button
import org.cubewhy.utils.ui.components.panel
import org.cubewhy.utils.ui.components.scrollPane
import java.awt.BorderLayout
import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JScrollPane

fun ComponentBuilder<*>.navBar(
    currentPage: ObservableState<String>,
) {
    panel {
        constraints(BorderLayout.NORTH)
        layout { borderLayout() }

        border { BorderFactory.createLineBorder(Color.GRAY) }

        scrollPane {
//            constraints(BorderLayout.EAST)

            verticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER)
            horizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS)


            viewPortView {
                layout { boxLayout(BoxLayout.X_AXIS) }

                // pages from plugins
                PageManager.externalPages.forEach { page ->
                    navigateButton(page, currentPage)
                }
            }
        }

        panel {
            constraints(BorderLayout.EAST)
            border { BorderFactory.createLineBorder(Color.GRAY) }

            // internal pages
            PageManager.internalPages.forEach { page ->
                navigateButton(page, currentPage)
            }
        }
    }
}

private fun ComponentBuilder<*>.navigateButton(page: Page, state: ObservableState<String>) {
    button {
        text(page.name)
        onClick {
            state.set(page::class.java.name)
        }
    }
}