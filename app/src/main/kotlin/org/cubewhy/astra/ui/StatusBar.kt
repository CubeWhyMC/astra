package org.cubewhy.astra.ui

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.cubewhy.astra.events.UpdateStatusEvent
import org.cubewhy.utils.ui.ComponentBuilder
import org.cubewhy.utils.ui.borderLayout
import org.cubewhy.utils.ui.components.label
import org.cubewhy.utils.ui.components.panel
import org.cubewhy.utils.ui.observableStateOf
import java.awt.BorderLayout
import java.util.concurrent.TimeUnit

fun ComponentBuilder<*>.statusBar() {
    panel {
        layout { borderLayout() }
        constraints(BorderLayout.SOUTH)

        val text = observableStateOf("")

        label { state(text) }

        handleEvent { e: UpdateStatusEvent ->
            text.set(e.text)

            runBlocking {
                launch {
                    delay(TimeUnit.SECONDS.toMillis(5))
                    text.set("") // clear status
                }
            }
        }
    }
}