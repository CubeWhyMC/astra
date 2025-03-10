package org.cubewhy.astra.plugins.impl.download

import org.cubewhy.astra.plugins.Plugin
import org.cubewhy.astra.plugins.annotations.Scan
import org.cubewhy.astra.ui.t

@Scan
class DownloadManagerPlugin : Plugin() {
    override val name: String = t("gui.plugins.download-manager.name")
    override val description: String = t("gui.plugins.download-manager.description")
    override val version: String = "1.0.0"
}