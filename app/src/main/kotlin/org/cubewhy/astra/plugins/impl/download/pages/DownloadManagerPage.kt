package org.cubewhy.astra.plugins.impl.download.pages

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.cubewhy.astra.events.DownloadStatus
import org.cubewhy.astra.events.FinishDownloadEvent
import org.cubewhy.astra.events.StartDownloadEvent
import org.cubewhy.astra.events.UpdateDownloadProgressEvent
import org.cubewhy.astra.plugins.Page
import org.cubewhy.astra.plugins.annotations.Scan
import org.cubewhy.astra.plugins.impl.download.DownloadManagerPlugin
import org.cubewhy.astra.ui.t
import org.cubewhy.utils.ui.*
import org.cubewhy.utils.ui.components.label
import org.cubewhy.utils.ui.components.list
import org.cubewhy.utils.ui.components.panel
import org.cubewhy.utils.ui.components.progressBar
import java.awt.BorderLayout
import java.awt.Component
import java.awt.GridBagConstraints
import java.io.File
import javax.swing.BorderFactory
import javax.swing.JProgressBar
import kotlin.math.roundToInt

@Scan
class DownloadManagerPage : Page {
    companion object {
        private val logger = KotlinLogging.logger {}
        private val scope = CoroutineScope(Dispatchers.IO)
    }

    override val name: String = t("gui.plugins.download-manager.pages.dm")

    override fun component() = panel {
        layout { borderLayout() }
        border { BorderFactory.createTitledBorder(this@DownloadManagerPage.name) }

        val files = ObservableList<DownloadTask>()

//        panel {
//            constraints(BorderLayout.NORTH)
//
//            layout { boxLayout(BoxLayout.X_AXIS) }
//            label { text("Test Download: ") }
//
//            val url = observableStateOf("")
//
//            textField {
//                state(url)
//
//                editable(true)
//                columns(20)
//            }
//
//            button {
//                text("Publish")
//
//                onClick {
//                    logger.info { "Test download: $url" }
//                    scope.launch {
//                    }
//                    url.set("")
//                }
//            }
//        }

        panel {
            constraints(BorderLayout.CENTER)
            layout { gridBagLayout() }

            list {
                alignmentX(Component.LEFT_ALIGNMENT)
                border { BorderFactory.createTitledBorder(t("gui.plugins.download-manager.pages.dm.list")) }
                constraints(GridBagConstraints().apply {
                    gridx = 0
                    gridy = 0
                    weightx = 1.0
                    weighty = 1.0
                    fill = GridBagConstraints.BOTH
                })

                cellRenderer { list, value: DownloadTask, index, isSelected, cellHasFocus ->
                    layout { borderLayout() }

                    if (isSelected) {
                        background(list.selectionBackground)
                        foreground(list.selectionForeground)
                    } else {
                        background(list.background)
                        foreground(list.foreground)
                    }

                    label {
                        constraints(BorderLayout.WEST)
                        text(value.file.name)
                    }

                    progressBar {
                        constraints(BorderLayout.EAST)
                        if (value.progress == -1.0) {
                            indeterminate(true)
                        } else {
                            minimum(0)
                            maximum(100)
                            value((value.progress * 100).roundToInt())
                        }
                        orientation(JProgressBar.HORIZONTAL)
                    }
                }

                listen(files)
            }
        }

        handleEvent { e: UpdateDownloadProgressEvent ->
            val task = files.find { it.fileId == e.fileId }
            task?.let {
                if (task.progress == -1.0) return@let
                if (e.progress <= 1) {
                    // update progress
                    task.progress = e.progress
                } else {
                    task.progress = -1.0
                }
                files[files.indexOf(task)] = task
            }
        }

        handleEvent { e: StartDownloadEvent ->
            DownloadManagerPlugin.astraBridge.updateStatus("Start download ${e.file.name}")
            files.add(DownloadTask(e.fileId, e.file, e.parts))
        }

        handleEvent { e: FinishDownloadEvent ->
            // find task
            val task = files.find { it.fileId == e.fileId }
            task?.let {
                if (e.status == DownloadStatus.SUCCESS) {
                    DownloadManagerPlugin.astraBridge.updateStatus("Success downloaded ${task.file.name}")
                } else {
                    DownloadManagerPlugin.astraBridge.updateStatus("Failed to downloaded ${task.file.name}")
                }
                files.remove(task)
            }
        }
    }.build()
}

private data class DownloadTask(
    val fileId: String,
    val file: File,
    val parts: Int,

    var progress: Double = 0.0
)