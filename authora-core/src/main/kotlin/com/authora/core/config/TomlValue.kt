package com.authora.core.config

sealed class TomlValue {
    data class Str(val value: String) : TomlValue()
    data class Bool(val value: Boolean) : TomlValue()
}