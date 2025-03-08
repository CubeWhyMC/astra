package org.cubewhy.astra.plugins.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ScanPackage(vararg val packages: String)
