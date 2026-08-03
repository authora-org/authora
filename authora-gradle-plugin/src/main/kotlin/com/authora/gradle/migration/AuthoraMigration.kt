package com.authora.gradle.migration

import java.io.File

interface AuthoraMigration {
    val fromVersion: String
    val toVersion: String
    fun migrate(tomlFile: File)
}