plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.kotlinPluginSerialization)
    alias(libs.plugins.shadow)

    // Apply the Application plugin to add support for building an executable JVM application.
    application
}

dependencies {
    // Project "app" depends on project "utils". (Project paths are separated with ":", so ":utils" refers to the top-level "utils" project.)
    implementation(project(":utils"))
    implementation(libs.flatlaf)
    implementation(libs.kotlin.reflect)
    implementation(libs.bundles.logging)
    implementation(libs.bundles.kotlinxEcosystem)
}

tasks.shadowJar {
    archiveClassifier.set("fatjar")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    exclude("native-binaries/**")

    exclude("LICENSE.txt")

    exclude("META-INF/maven/**")
    exclude("META-INF/versions/**")

    exclude("org/junit/**")
}

tasks.jar {
    dependsOn("shadowJar")
}


application {
    // Define the Fully Qualified Name for the application main class
    // (Note that Kotlin compiles `App.kt` to a class with FQN `com.example.app.AppKt`.)
    mainClass = "org.cubewhy.astra.AstraKt"
}
