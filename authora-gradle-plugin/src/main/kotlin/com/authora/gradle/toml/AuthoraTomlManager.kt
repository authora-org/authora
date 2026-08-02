package com.authora.gradle.toml

import java.io.File

object AuthoraTomlManager {

    fun load(file: File): LinkedHashMap<String, LinkedHashMap<String, TomlValue>> {
        if (!file.exists()) return LinkedHashMap()
        return AuthoraTomlFile.parse(file.readText())
    }

    fun save(file: File, sections: LinkedHashMap<String, LinkedHashMap<String, TomlValue>>) {
        file.writeText(AuthoraTomlFile.serialize(sections))
    }

    fun replaceSection(file: File, sectionName: String, values: LinkedHashMap<String, TomlValue>) {
        val sections = load(file)
        sections[sectionName] = values
        save(file, sections)
    }

    fun getString(file: File, section: String, key: String): String? {
        val value = load(file)[section]?.get(key) ?: return null
        return (value as? TomlValue.Str)?.value
    }

    fun getBool(file: File, section: String, key: String): Boolean? {
        val value = load(file)[section]?.get(key) ?: return null
        return (value as? TomlValue.Bool)?.value
    }
}