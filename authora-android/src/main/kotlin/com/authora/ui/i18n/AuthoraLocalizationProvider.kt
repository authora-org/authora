package com.authora.ui.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.authora.core.config.AuthoraConfigException
import com.authora.core.config.AuthoraConfigLoader
import com.authora.core.i18n.AuthoraLocaleResolver
import com.authora.core.i18n.AuthoraStringsCatalog

@Composable
fun AuthoraLocalizationProvider(content: @Composable () -> Unit) {
    val context = LocalContext.current

    val strings = remember {
        try {
            val config = AuthoraConfigLoader.load(context)
            AuthoraLocaleResolver.resolve(config.language)
        } catch (e: AuthoraConfigException) {
            AuthoraStringsCatalog.english
        }
    }

    CompositionLocalProvider(LocalAuthoraStrings provides strings) {
        content()
    }
}