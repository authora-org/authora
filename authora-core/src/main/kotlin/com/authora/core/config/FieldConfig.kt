package com.authora.core.config

data class FieldConfig(
    val name: String,
    val type: FieldType,
    val required: Boolean,
    val label: String?
) {
    companion object {
        fun from(entry: LinkedHashMap<String, TomlValue>): FieldConfig {
            val name = (entry["name"] as? TomlValue.Str)?.value
                ?: throw AuthoraConfigException("Every [[fields]] entry must declare a 'name'.")
            val explicitType = (entry["type"] as? TomlValue.Str)?.value
            val type = explicitType?.let { FieldType.fromId(it) } ?: FieldType.forFieldName(name)
            val required = (entry["required"] as? TomlValue.Bool)?.value ?: FieldCatalog.isRequiredByDefault(name)
            val label = (entry["label"] as? TomlValue.Str)?.value
            return FieldConfig(name, type, required, label)
        }
    }
}