package com.authora.core.config

enum class FieldType(val id: String) {
    TEXT("text"),
    EMAIL("email"),
    PASSWORD("password"),
    PHONE("phone"),
    URL("url"),
    USERNAME("username");

    companion object {
        fun fromId(id: String): FieldType = entries.firstOrNull { it.id == id } ?: TEXT

        fun forFieldName(name: String): FieldType = when (name) {
            "email" -> EMAIL
            "password", "confirm_password" -> PASSWORD
            "phone" -> PHONE
            "website", "github", "microsoft", "discord", "telegram", "linkedin" -> URL
            "username" -> USERNAME
            else -> TEXT
        }
    }
}