package org.cubewhy.astra.configs

import java.util.*

enum class Language(val localizedString: String, val locale: Locale) {
    ENGLISH("English", Locale.ENGLISH),
    SIMPLIFIED_CHINESE("简体中文", Locale.SIMPLIFIED_CHINESE),
    TRADITIONAL_CHINESE("繁體中文", Locale.TRADITIONAL_CHINESE);

    override fun toString(): String {
        return localizedString
    }
}