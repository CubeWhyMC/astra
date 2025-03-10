package org.cubewhy.utils

enum class HashAlgorithm(val algorithmName: String) {
    SHA256("SHA-256"),
    SHA1("SHA-1"),
    MD5("MD5");

    companion object {
        fun fromString(name: String): HashAlgorithm? {
            return entries.find { it.algorithmName.equals(name, ignoreCase = true) }
        }
    }
}