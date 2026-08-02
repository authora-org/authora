package com.authora.gradle.toml

import com.authora.gradle.AuthoraConfigException

object AuthoraTomlFile {

    fun parse(text: String): LinkedHashMap<String, LinkedHashMap<String, TomlValue>> {
        val sections = LinkedHashMap<String, LinkedHashMap<String, TomlValue>>()
        var currentSection: String? = null

        text.lineSequence().forEachIndexed { index, rawLine ->
            val line = rawLine.trim()
            if (line.isEmpty() || line.startsWith("#")) return@forEachIndexed

            if (line.startsWith("[") && line.endsWith("]")) {
                val name = line.substring(1, line.length - 1).trim()
                currentSection = name
                sections.getOrPut(name) { LinkedHashMap() }
                return@forEachIndexed
            }

            val separatorIndex = line.indexOf('=')
            if (separatorIndex < 0) {
                throw AuthoraConfigException("Invalid Authora.toml at line ${index + 1}: '$rawLine'")
            }

            val key = line.substring(0, separatorIndex).trim()
            val rawValue = line.substring(separatorIndex + 1).trim()
            val section = currentSection
                ?: throw AuthoraConfigException("Invalid Authora.toml at line ${index + 1}: key '$key' is outside of any section.")

            sections[section]!![key] = parseValue(rawValue)
        }

        return sections
    }

    private fun parseValue(raw: String): TomlValue = when {
        raw == "true" -> TomlValue.Bool(true)
        raw == "false" -> TomlValue.Bool(false)
        raw.length >= 2 && raw.startsWith("\"") && raw.endsWith("\"") ->
            TomlValue.Str(raw.substring(1, raw.length - 1).replace("\\\"", "\"").replace("\\\\", "\\"))
        else -> TomlValue.Str(raw)
    }

    fun serialize(sections: LinkedHashMap<String, LinkedHashMap<String, TomlValue>>): String {
        val builder = StringBuilder()
        for ((sectionName, values) in sections) {
            builder.append("[").append(sectionName).append("]\n")
            for ((key, value) in values) {
                builder.append(key).append(" = ").append(value.toString()).append("\n")
            }
            builder.append("\n")
        }
        return builder.toString().trimEnd().plus("\n")
    }
}