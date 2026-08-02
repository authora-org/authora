package com.authora.core.config

import android.content.Context
import java.io.IOException

object AuthoraConfigLoader {
    fun load(context: Context, assetFileName: String = "Authora.toml"): AuthoraConfig {
        val text = try {
            context.assets.open(assetFileName).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            throw AuthoraConfigException(
                "Could not read '$assetFileName' from app assets. Make sure the Authora Gradle Plugin is applied so Authora.toml is synced into assets."
            )
        }

        val document = AuthoraTomlParser.parse(text)
        return AuthoraConfig.from(document)
    }
}