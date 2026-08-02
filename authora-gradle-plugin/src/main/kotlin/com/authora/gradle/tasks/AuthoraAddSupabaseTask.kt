package com.authora.gradle.tasks

import com.authora.gradle.AuthoraConfigException
import com.authora.gradle.toml.AuthoraTomlManager
import com.authora.gradle.toml.TomlValue
import com.authora.gradle.util.ConsolePrompter
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class AuthoraAddSupabaseTask : DefaultTask() {

    init {
        group = "authora"
        description = "Connects Supabase to Authora"
    }

    @TaskAction
    fun run() {
        println("Authora Setup")
        println("Supabase provider.\n")

        val url = ConsolePrompter.ask("Supabase URL")
        if (url.isBlank() || !url.startsWith("https://")) {
            throw AuthoraConfigException("Invalid Supabase URL. It must start with https://")
        }

        val anonKey = ConsolePrompter.ask("Supabase Anon Key")
        if (anonKey.isBlank()) {
            throw AuthoraConfigException("Supabase Anon Key cannot be empty.")
        }
        if (anonKey.split(".").size != 3) {
            println("⚠ The Anon Key does not look like a valid JWT, but it will be saved anyway.")
        }

        val tomlFile = project.file("Authora.toml")
        AuthoraTomlManager.replaceSection(
            tomlFile, "auth.provider",
            linkedMapOf(
                "type" to TomlValue.Str("supabase"),
                "url" to TomlValue.Str(url),
                "anon_key" to TomlValue.Str(anonKey)
            )
        )

        println("\n✔ Supabase connected to Authora.")
        println("✔ Authora.toml updated.")
        println("\nRe-run Gradle sync so Authora can add the Supabase dependencies automatically.")
    }
}