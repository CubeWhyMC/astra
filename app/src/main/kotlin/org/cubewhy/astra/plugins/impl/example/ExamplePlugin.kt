package org.cubewhy.astra.plugins.impl.example

import org.cubewhy.astra.plugins.Plugin
import org.cubewhy.astra.plugins.annotations.Scan

@Scan
class ExamplePlugin : Plugin() {
    override val name: String = "Example Plugin"
}