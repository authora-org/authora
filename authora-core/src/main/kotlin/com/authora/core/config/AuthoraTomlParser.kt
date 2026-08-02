package com.authora.core.config

object AuthoraTomlParser {
    fun parse(text: String): AuthoraTomlDocument {
        val sections = LinkedHashMap<String, LinkedHashMap<String, TomlValue>>()
        val arrayTables = LinkedHashMap<String, MutableList<LinkedHashMap<String, TomlValue>>>()
        var currentTarget: LinkedHashMap<String, TomlValue>? = null

        text.lineSequence().forEachIndexed { index, rawLine ->
            val line = rawLine.trim()
            if (line.isEmpty() || line.startsWith("#")) return@forEachIndexed

            if (line.startsWith("[[") && line.endsWith("]]")) {
                val name = line.substring(2, line.length - 2).trim()
                val entry = LinkedHashMap<String, TomlValue>()
                arrayTables.getOrPut(name) { mutableListOf() }.add(entry)
                currentTarget = entry
                return@forEachIndexed
            }

            if (line.startsWith("[") && line.endsWith("]")) {
                val name = line.substring(1, line.length - 1).trim()
                currentTarget = sections.getOrPut(name) { LinkedHashMap() }
                return@forEachIndexed
            }

            val separatorIndex = line.indexOf('=')
            if (separatorIndex < 0) {
                throw AuthoraConfigException("Invalid Authora.toml at line ${index + 1}: '$rawLine'")
            }

            val key = line.substring(0, separatorIndex).trim()
            val rawValue = line.substring(separatorIndex + 1).trim()
            val target = currentTarget
                ?: throw AuthoraConfigException("Invalid Authora.toml at line ${index + 1}: key '$key' is outside of any section.")

            target[key] = parseValue(rawValue)
        }

        return AuthoraTomlDocument(sections, arrayTables)
    }

    private fun parseValue(raw: String): TomlValue = when {
        raw == "true" -> TomlValue.Bool(true)
        raw == "false" -> TomlValue.Bool(false)
        raw.length >= 2 && raw.startsWith("\"") && raw.endsWith("\"") ->
            TomlValue.Str(raw.substring(1, raw.length - 1).replace("\\\"", "\"").replace("\\\\", "\\"))
        else -> TomlValue.Str(raw)
    }
}

data class AuthoraTomlDocument(
    val sections: LinkedHashMap<String, LinkedHashMap<String, TomlValue>>,
    val arrayTables: LinkedHashMap<String, MutableList<LinkedHashMap<String, TomlValue>>>
)