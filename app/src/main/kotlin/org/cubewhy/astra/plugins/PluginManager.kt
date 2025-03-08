package org.cubewhy.astra.plugins

import io.github.oshai.kotlinlogging.KotlinLogging
import org.cubewhy.astra.events.EventBus
import org.cubewhy.astra.events.EventHandler
import org.cubewhy.astra.events.PostInitEvent
import org.cubewhy.astra.pages.PageManager
import org.cubewhy.astra.plugins.annotations.PostInit
import org.cubewhy.astra.plugins.annotations.PreInit
import org.cubewhy.astra.plugins.annotations.ScanPackage
import org.cubewhy.astra.plugins.annotations.Unload
import org.cubewhy.utils.ClassScanner
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation

object PluginManager {
    private val logger = KotlinLogging.logger {}
    private val plugins = mutableListOf<Plugin>()

    private val scanPackages = mutableSetOf<String>()

    init {
        EventBus.register(this)
    }

    internal fun registerPlugin(plugin: Class<out Plugin>) {
        // init plugin
        logger.info { "Registered plugin ${plugin.name}" }
        val instance = plugin.declaredConstructors.first { it.parameterCount == 0 }.newInstance() as Plugin
        // create bridge
        instance.bridge = AstraBridgeImpl(instance)
        // exec pre-init method
        findPreInitMethods(instance::class).forEach { method -> method.call(instance) }
        // register with eventBus
        EventBus.register(instance)
        // register component scan
        scanPackages.add(plugin.packageName)
        // read @ScanPackage annotation
        scanPackages.addAll(findScanPackages(plugin))
        plugins.add(instance)
    }

    internal fun loadPlugins() {
        logger.info { "Loading plugins..." }
        // scan packages
        scanPackages.forEach { scanPackage ->
            ClassScanner.scanRegisteredClasses(scanPackage).forEach { clazz ->
                val instance = clazz.createInstance()
                // register with eventBus
                EventBus.register(instance)
                if (clazz.java.interfaces.contains(Page::class.java)) {
                    logger.info { "Register page ${clazz.java.name}" }
                    PageManager.registerPage(instance as Page)
                }
            }
        }
    }

    internal fun unloadPlugins() {
        logger.info { "Unloading plugins..." }
        plugins.forEach { plugin ->
            unloadPlugin(plugin)
        }
    }

    internal fun unloadPlugin(plugin: Plugin) {
        logger.info { "Unloading plugin ${plugin::class.java.name}" }
        // invoke unload method
        findUnloadMethods(plugin::class).forEach { method ->
            method.call(plugin)
        }
    }

    @EventHandler
    internal suspend fun onPostInit(event: PostInitEvent) {
        plugins.forEach { plugin ->
            // exec post-init method
            findPostInitMethods(plugin::class).forEach { method -> method.callSuspend(plugin) }
        }
    }

    private fun findPreInitMethods(plugin: KClass<out Plugin>): List<KFunction<*>> {
        return plugin.members
            .filterIsInstance<KFunction<*>>()
            .filter { it.findAnnotation<PreInit>() != null }
    }

    private fun findPostInitMethods(plugin: KClass<out Plugin>): List<KFunction<*>> {
        return plugin.members
            .filterIsInstance<KFunction<*>>()
            .filter { it.findAnnotation<PostInit>() != null }
    }

    private fun findUnloadMethods(plugin: KClass<out Plugin>): List<KFunction<*>> {
        return plugin.members
            .filterIsInstance<KFunction<*>>()
            .filter { it.findAnnotation<Unload>() != null }
    }

    private fun findScanPackages(clazz: Class<*>): List<String> {
        return clazz.kotlin.findAnnotation<ScanPackage>()?.packages?.toList() ?: emptyList()
    }

}