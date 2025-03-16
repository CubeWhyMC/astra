package org.cubewhy.utils

data class Platform(
    val os: OperatingSystem,
    val version: String,
    val arch: Architecture
) {
    companion object {
        val current: Platform by lazy {
            Platform(
                os = OperatingSystem.current,
                version = System.getProperty("os.version"),
                arch = Architecture.current
            )
        }
    }
}

enum class OperatingSystem(val requestName: String) {
    LINUX("linux"), WINDOWS("windows"), MACOS("darwin");

    companion object {
        val current: OperatingSystem by lazy {
            val osName = System.getProperty("os.name").lowercase()
            return@lazy when {
                osName.contains("win") -> WINDOWS
                osName.contains("mac") -> MACOS
                osName.contains("nix") || osName.contains("nux") || osName.contains("aix") -> LINUX
                else -> throw IllegalStateException("Unknown operating system: $osName")
            }
        }
    }
}

enum class Architecture(val requestName: String) {
    AMD64("x64"),
    X86("x86"),
    ARM64("arm64"),
    ARM("arm");

    companion object {
        val current: Architecture by lazy {
            val arch = System.getProperty("os.arch").lowercase()
            return@lazy when {
                arch == "amd64" || arch == "x86_64" -> AMD64
                arch == "x86" || arch == "i386" || arch == "i686" -> X86
                arch == "aarch64" || arch == "arm64" -> ARM64
                arch.startsWith("arm") -> ARM
                else -> throw IllegalArgumentException("Unsupported arch: $arch")
            }
        }
    }
}
