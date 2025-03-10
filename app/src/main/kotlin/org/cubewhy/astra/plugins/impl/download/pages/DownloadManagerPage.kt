package org.cubewhy.astra.plugins.impl.download.pages

import org.cubewhy.astra.plugins.Page
import org.cubewhy.astra.plugins.annotations.Scan
import org.cubewhy.astra.ui.t
import org.cubewhy.utils.ui.components.panel
import javax.swing.BorderFactory

@Scan
class DownloadManagerPage : Page {
    override val name: String = t("gui.plugins.download-manager.pages.dm")

    override fun component() = panel {
        border { BorderFactory.createTitledBorder(this@DownloadManagerPage.name) }

        // todo download manager
//        handleEvent { e: StartDownloadEvent ->
//
//        }
    }.build()
}