package com.authora.core.config

data class AuthoraConfig(
    val version: String,
    val language: String,
    val provider: ProviderConfig,
    val ui: UiConfig,
    val fields: List<FieldConfig>
) {
    companion object {
        fun from(document: AuthoraTomlDocument): AuthoraConfig {
            val sections = document.sections
            val authoraSection = sections["authora"]
            val version = (authoraSection?.get("version") as? TomlValue.Str)?.value ?: "0.1.0"
            val language = (authoraSection?.get("language") as? TomlValue.Str)?.value ?: "auto"

            val providerSection = sections["auth.provider"]
                ?: throw AuthoraConfigException("Missing [auth.provider] section in Authora.toml.")
            val provider = ProviderConfig.from(providerSection)

            val uiSection = sections["ui"] ?: LinkedHashMap()
            val themeSection = sections["ui.theme"] ?: LinkedHashMap()
            val ui = UiConfig.from(uiSection, themeSection)

            val fields = (document.arrayTables["fields"] ?: emptyList()).map { FieldConfig.from(it) }

            return AuthoraConfig(version, language, provider, ui, fields)
        }
    }
}

sealed class ProviderConfig {
    data class Firebase(val managedConfig: Boolean, val configName: String) : ProviderConfig()
    data class Supabase(val url: String, val anonKey: String) : ProviderConfig()

    companion object {
        fun from(section: LinkedHashMap<String, TomlValue>): ProviderConfig {
            val type = (section["type"] as? TomlValue.Str)?.value
                ?: throw AuthoraConfigException("Missing 'type' in [auth.provider] section.")

            return when (type) {
                "firebase" -> Firebase(
                    managedConfig = (section["managed_config"] as? TomlValue.Bool)?.value ?: true,
                    configName = (section["config_name"] as? TomlValue.Str)?.value ?: "default"
                )
                "supabase" -> Supabase(
                    url = (section["url"] as? TomlValue.Str)?.value
                        ?: throw AuthoraConfigException("Missing 'url' for Supabase provider."),
                    anonKey = (section["anon_key"] as? TomlValue.Str)?.value
                        ?: throw AuthoraConfigException("Missing 'anon_key' for Supabase provider.")
                )
                else -> throw AuthoraConfigException("Unknown provider type '$type' in Authora.toml.")
            }
        }
    }
}

data class UiConfig(
    val appName: String,
    val appIcon: String,
    val theme: ThemeConfig
) {
    companion object {
        fun from(uiSection: LinkedHashMap<String, TomlValue>, themeSection: LinkedHashMap<String, TomlValue>): UiConfig {
            return UiConfig(
                appName = (uiSection["app_name"] as? TomlValue.Str)?.value ?: "",
                appIcon = (uiSection["app_icon"] as? TomlValue.Str)?.value ?: "auto",
                theme = ThemeConfig.from(themeSection)
            )
        }
    }
}

data class ThemeConfig(
    val dynamicColor: Boolean,
    val darkMode: String
) {
    companion object {
        fun from(section: LinkedHashMap<String, TomlValue>): ThemeConfig {
            return ThemeConfig(
                dynamicColor = (section["dynamic_color"] as? TomlValue.Bool)?.value ?: true,
                darkMode = (section["dark_mode"] as? TomlValue.Str)?.value ?: "system"
            )
        }
    }
}