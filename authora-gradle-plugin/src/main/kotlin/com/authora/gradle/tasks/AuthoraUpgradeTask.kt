package com.authora.gradle.tasks

import com.authora.gradle.AuthoraConfigException
import com.authora.gradle.AuthoraVersions
import com.authora.gradle.migration.AuthoraMigrationRegistry
import com.authora.gradle.toml.AuthoraTomlManager
import com.authora.gradle.toml.TomlValue
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

abstract class AuthoraUpgradeTask : DefaultTask() {

    init {
        group = "authora"
        description = "Checks for and applies Authora version upgrades, running any required migrations"
    }

    @TaskAction
    fun run() {
        val tomlFile = project.file("Authora.toml")
        val currentVersion = AuthoraTomlManager.getString(tomlFile, "authora", "version") ?: AuthoraVersions.RUNTIME_DEFAULT

        println("Current Authora version: $currentVersion")
        println("Checking for the latest version...")

        val latestVersion = try {
            fetchLatestVersion()
        } catch (e: Exception) {
            throw AuthoraConfigException("Failed to check the latest Authora version.\nDetails: ${e.message}")
        } ?: throw AuthoraConfigException("Could not find Authora version information on Maven Central.")

        if (compareVersions(latestVersion, currentVersion) <= 0) {
            println("✔ Authora is already up to date ($currentVersion).")
            return
        }

        val migrations = AuthoraMigrationRegistry.pathFrom(currentVersion, latestVersion)

        if (migrations.isNotEmpty()) {
            val backupFile = project.file("Authora.toml.bak")
            tomlFile.copyTo(backupFile, overwrite = true)
            println("✔ Backed up Authora.toml to ${backupFile.name}")

            migrations.forEach { migration ->
                println("Applying migration ${migration.fromVersion} -> ${migration.toVersion}...")
                try {
                    migration.migrate(tomlFile)
                } catch (e: Exception) {
                    backupFile.copyTo(tomlFile, overwrite = true)
                    throw AuthoraConfigException(
                        "Migration ${migration.fromVersion} -> ${migration.toVersion} failed. Authora.toml was restored from backup.\nDetails: ${e.message}"
                    )
                }
            }
        }

        AuthoraTomlManager.replaceSection(
            tomlFile, "authora",
            linkedMapOf(
                "version" to TomlValue.Str(latestVersion),
                "language" to TomlValue.Str(
                    AuthoraTomlManager.getString(tomlFile, "authora", "language") ?: "auto"
                )
            )
        )

        println("✔ Authora upgraded from $currentVersion to $latestVersion.")
        if (migrations.isNotEmpty()) {
            println("✔ ${migrations.size} migration(s) applied.")
        }
        println("Re-run Gradle sync to apply the new version.")
    }

    private fun fetchLatestVersion(): String? {
        val url = URL("https://search.maven.org/solrsearch/select?q=g:io.github.authora-org+AND+a:authora-android&rows=1&wt=json")
        val connection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        connection.requestMethod = "GET"

        val responseText = connection.inputStream.bufferedReader().use { it.readText() }
        connection.disconnect()

        val json = JSONObject(responseText)
        val response = json.optJSONObject("response") ?: return null
        val docs = response.optJSONArray("docs") ?: return null
        if (docs.length() == 0) return null
        val version = docs.getJSONObject(0).optString("latestVersion", "")
        return version.ifBlank { null }
    }

    private fun compareVersions(a: String, b: String): Int {
        val partsA = a.split(".").map { it.toIntOrNull() ?: 0 }
        val partsB = b.split(".").map { it.toIntOrNull() ?: 0 }
        val size = maxOf(partsA.size, partsB.size)
        for (i in 0 until size) {
            val x = partsA.getOrElse(i) { 0 }
            val y = partsB.getOrElse(i) { 0 }
            if (x != y) return x.compareTo(y)
        }
        return 0
    }
}