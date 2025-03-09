package org.cubewhy.utils

import org.cubewhy.astra.plugins.annotations.Scan
import java.io.File
import java.net.URL
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

/**
 * Utility object for scanning classes in a given package for specific annotations.
 *
 * This object is used to scan all classes within a package and its sub-packages for classes that are annotated
 * with the [Scan] annotation. It uses the class loader to locate resources and directories in the classpath.
 */
object ClassScanner {

    /**
     * Scans the specified package for classes annotated with the [Scan] annotation.
     *
     * This method retrieves all resources for the given package and iterates over them. If a resource is a directory,
     * it recursively scans the directory for class files. It then loads the classes and checks if they are annotated with
     * the [Scan] annotation.
     *
     * @param packageName The package name to scan (default is an empty string, which means the root of the classpath).
     * @return A set of [KClass] objects representing the classes that are annotated with [Scan].
     */
    fun scanRegisteredClasses(packageName: String = ""): Set<KClass<*>> {
        val annotatedClasses = mutableSetOf<KClass<*>>() // Set to hold the annotated classes

        // Convert the package name to a file path
        val packagePath = packageName.replace(".", "/")
        val classLoader = Thread.currentThread().contextClassLoader

        // Get all resources for the specified package
        val resources: Enumeration<URL> = classLoader.getResources(packagePath)
        while (resources.hasMoreElements()) {
            val resource = resources.nextElement()
            // Process only file resources (directories in this case)
            if (resource.protocol == "file") {
                val directory = File(resource.file)
                if (directory.exists() && directory.isDirectory) {
                    // Recursively scan the directory for class files
                    scanDirectory(directory, packageName, annotatedClasses)
                }
            }
        }

        return annotatedClasses // Return the set of annotated classes
    }

    /**
     * Recursively scans a directory for class files and checks if they are annotated with [Scan].
     *
     * This method checks each file in the given directory. If the file is a class file (ending with ".class"),
     * it attempts to load the class and checks for the presence of the [Scan] annotation. If the class is annotated,
     * it is added to the [annotatedClasses] set.
     *
     * @param directory The directory to scan.
     * @param packageName The current package name (used for fully qualified class names).
     * @param annotatedClasses The mutable set to hold classes with the [Scan] annotation.
     */
    private fun scanDirectory(directory: File, packageName: String, annotatedClasses: MutableSet<KClass<*>>) {
        // Iterate over all files in the directory
        directory.listFiles()?.forEach { file ->
            // If the file is a directory, recursively scan it
            if (file.isDirectory) {
                if (packageName.isEmpty()) {
                    scanDirectory(file, file.name, annotatedClasses) // Scan subpackage
                } else {
                    scanDirectory(file, "$packageName.${file.name}", annotatedClasses) // Scan nested subpackage
                }
            } else if (file.name.endsWith(".class")) {
                // If the file is a class file, attempt to load it and check for the [Scan] annotation
                val className = "$packageName.${file.name.removeSuffix(".class")}"
                try {
                    // Load the class and check if it has the [Scan] annotation
                    val clazz = Class.forName(className).kotlin
                    if (clazz.findAnnotation<Scan>() != null) {
                        annotatedClasses.add(clazz) // Add the class to the set if it is annotated
                    }
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace() // Log if the class could not be found
                }
            }
        }
    }
}
