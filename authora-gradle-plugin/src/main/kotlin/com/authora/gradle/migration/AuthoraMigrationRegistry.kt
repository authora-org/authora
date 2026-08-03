package com.authora.gradle.migration

object AuthoraMigrationRegistry {
    private val migrations: List<AuthoraMigration> = listOf(
        AddLanguageKeyMigration()
        // Register future migrations here, in order, each starting where the previous one ends.
    )

    fun pathFrom(currentVersion: String, targetVersion: String): List<AuthoraMigration> {
        val path = mutableListOf<AuthoraMigration>()
        var cursor = currentVersion
        while (cursor != targetVersion) {
            val next = migrations.firstOrNull { it.fromVersion == cursor } ?: break
            path.add(next)
            cursor = next.toVersion
        }
        return path
    }
}