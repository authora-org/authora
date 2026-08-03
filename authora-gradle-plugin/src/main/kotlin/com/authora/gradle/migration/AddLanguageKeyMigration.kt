package com.authora.gradle.migration

import com.authora.gradle.toml.AuthoraTomlManager
import com.authora.gradle.toml.TomlValue
import java.io.File

/**
 * Ensures the [authora] section has a 'language' key, introduced in 0.2.0.
 * Projects created before this version only had 'version' under [authora].
 */
class AddLanguageKeyMigration : AuthoraMigration {
    override val fromVersion = "0.1.0"
    override val toVersion = "0.2.0"

    override fun migrate(tomlFile: File) {
        val sections = AuthoraTomlManager.load(tomlFile)
        val authoraSection = sections.getOrPut("authora") { LinkedHashMap() }
        if (!authoraSection.containsKey("language")) {
            authoraSection["language"] = TomlValue.Str("auto")
        }
        AuthoraTomlManager.save(tomlFile, sections)
    }
}