package org.cubewhy.astra.events

import org.cubewhy.astra.plugins.Plugin
import org.cubewhy.astra.plugins.PluginState
import java.io.File

class PostInitEvent: Event

class TogglePluginEvent(val plugin: Plugin, val state: PluginState) : Event

class UpdateStatusEvent(val text: String) : Event

enum class DownloadStatus {
    SUCCESS, FAILURE
}

class StartDownloadEvent(val fileId: String, val file: File, val parts: Int) : Event
class UpdateDownloadProgressEvent(val fileId: String, val progress: Double) : Event
class FinishDownloadEvent(val fileId: String, val status: DownloadStatus) : Event
