package com.authora.core.i18n

import java.util.Locale

object AuthoraLocaleResolver {
    fun resolve(configLanguage: String, deviceLanguageCode: String = Locale.getDefault().language): AuthoraStrings {
        if (configLanguage != "auto") {
            return AuthoraStringsCatalog.forLanguageCode(configLanguage) ?: AuthoraStringsCatalog.english
        }
        return AuthoraStringsCatalog.forLanguageCode(deviceLanguageCode) ?: AuthoraStringsCatalog.english
    }
}