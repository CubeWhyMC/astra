package org.cubewhy.utils

import org.cubewhy.astra.plugins.annotations.Scan
import java.io.File
import java.net.URL
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

object ClassScanner {

    fun scanRegisteredClasses(packageName: String = ""): Set<KClass<*>> {
        val annotatedClasses = mutableSetOf<KClass<*>>()

        val packagePath = packageName.replace(".", "/")
        val classLoader = Thread.currentThread().contextClassLoader

        val resources: Enumeration<URL> = classLoader.getResources(packagePath)
        while (resources.hasMoreElements()) {
            val resource = resources.nextElement()
            if (resource.protocol == "file") {
                val directory = File(resource.file)
                if (directory.exists() && directory.isDirectory) {
                    scanDirectory(directory, packageName, annotatedClasses)
                }
            }
        }

        return annotatedClasses
    }

    private fun scanDirectory(directory: File, packageName: String, annotatedClasses: MutableSet<KClass<*>>) {
        directory.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                if (packageName.isEmpty()) {
                    scanDirectory(file, file.name, annotatedClasses)
                } else {
                    scanDirectory(file, "$packageName.${file.name}", annotatedClasses)
                }
            } else if (file.name.endsWith(".class")) {
                val className = "$packageName.${file.name.removeSuffix(".class")}"
                try {
                    val clazz = Class.forName(className).kotlin
                    if (clazz.findAnnotation<Scan>() != null) {
                        annotatedClasses.add(clazz)
                    }
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }
}