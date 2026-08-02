package com.authora.gradle.tasks

import com.authora.gradle.AuthoraConfigException
import com.authora.gradle.firebase.FirebaseConfigValidator
import com.authora.gradle.toml.AuthoraTomlManager
import com.authora.gradle.toml.TomlValue
import com.authora.gradle.util.AndroidProjectInspector
import com.authora.gradle.util.ConsolePrompter
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class AuthoraAddFirebaseTask : DefaultTask() {

    init {
        group = "authora"
        description = "Connects Firebase to Authora via google-services.json"
    }

    @TaskAction
    fun run() {
        println("Authora Setup")
        println("Firebase detected.\n")

        val input = ConsolePrompter.ask(
            "Please enter the path to your google-services.json\n(or leave blank to paste JSON directly)"
        )

        val jsonText = if (input.isNotBlank()) {
            val file = project.file(input)
            if (!file.exists()) throw AuthoraConfigException("File not found: ${file.absolutePath}")
            file.readText()
        } else {
            ConsolePrompter.askMultiline("Paste the contents of google-services.json below:")
        }

        if (jsonText.isBlank()) throw AuthoraConfigException("Firebase configuration is empty. Aborting.")

        val applicationId = AndroidProjectInspector.findApplicationId(project)
        val result = FirebaseConfigValidator.validate(jsonText, applicationId)
        val projectId = when (result) {
            is FirebaseConfigValidator.Result.Failure ->
                throw AuthoraConfigException("Invalid Firebase configuration.\nReason: ${result.reason}")
            is FirebaseConfigValidator.Result.Success -> result.projectId
        }

        val configName = ConsolePrompter.ask("Configuration name (default: default)").ifBlank { "default" }

        val googleServicesFile = project.file("google-services.json")
        googleServicesFile.writeText(jsonText)

        val managedDir = project.file("authora/config")
        managedDir.mkdirs()
        File(managedDir, "firebase-$configName.json").writeText(jsonText)

        val tomlFile = project.file("Authora.toml")
        AuthoraTomlManager.replaceSection(
            tomlFile, "auth.provider",
            linkedMapOf(
                "type" to TomlValue.Str("firebase"),
                "managed_config" to TomlValue.Bool(true),
                "config_name" to TomlValue.Str(configName)
            )
        )

        println("\n✔ Firebase project '$projectId' connected to Authora.")
        println("✔ google-services.json saved to: ${googleServicesFile.absolutePath}")
        println("✔ Authora.toml updated.")
        println("\nRe-run Gradle sync so Authora can add the Firebase dependencies automatically.")
    }
}