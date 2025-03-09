package org.cubewhy.astra.ui

import io.github.oshai.kotlinlogging.KotlinLogging
import org.cubewhy.astra.configs.Language
import java.util.*

object Translate {
    private val logger = KotlinLogging.logger {}

    lateinit var bundle: ResourceBundle
    private var locale = Language.ENGLISH.locale
        set(value) {
            bundle = ResourceBundle.getBundle("astra", value)
            field = value
        }

    fun useLanguage(language: Language) {
        locale = language.locale
        logger.info { "Set language to ${locale.displayName} (${language.localizedString})" }
    }
}


fun t(key: String): String {
    return Translate.bundle.getStringOrNull(key) ?: key
}

private fun ResourceBundle.getStringOrNull(key: String): String? {
    return try {
        this.getString(key)
    } catch (ignored: MissingResourceException) {
        null
    }
}
