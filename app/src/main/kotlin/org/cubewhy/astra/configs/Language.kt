package org.cubewhy.astra.configs

enum class Language(val localizedString: String) {
    ENGLISH("English"),
    SIMPLIFIED_CHINESE("简体中文"),
    TRADITIONAL_CHINESE("繁體中文");

    override fun toString(): String {
        return localizedString
    }
}