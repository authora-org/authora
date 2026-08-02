package com.authora.gradle.toml

sealed class TomlValue {
    data class Str(val value: String) : TomlValue()
    data class Bool(val value: Boolean) : TomlValue()

    override fun toString(): String = when (this) {
        is Str -> "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
        is Bool -> value.toString()
    }
}