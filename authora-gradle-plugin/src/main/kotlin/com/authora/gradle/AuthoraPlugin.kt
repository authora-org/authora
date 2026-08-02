package com.authora.gradle

import java.io.File
import com.authora.gradle.tasks.AuthoraAddFirebaseTask
import com.authora.gradle.tasks.AuthoraAddSupabaseTask
import com.authora.gradle.tasks.AuthoraPreviewTask
import com.authora.gradle.tasks.AuthoraUpgradeTask
import com.authora.gradle.toml.AuthoraTomlManager
import com.google.gms.googleservices.GoogleServicesPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class AuthoraPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.tasks.register("authoraAddFirebase", AuthoraAddFirebaseTask::class.java)
        project.tasks.register("authoraAddSupabase", AuthoraAddSupabaseTask::class.java)
        project.tasks.register("authoraPreview", AuthoraPreviewTask::class.java)
        project.tasks.register("authoraUpgrade", AuthoraUpgradeTask::class.java)

        val tomlFile = project.file("Authora.toml")
        if (!tomlFile.exists()) {
            project.logger.lifecycle(
                "[Authora] Authora.toml not found in ${project.name}. Run 'authoraAddFirebase' or 'authoraAddSupabase' to get started."
            )
            return
        }

        val runtimeVersion = AuthoraTomlManager.getString(tomlFile, "authora", "version") ?: AuthoraVersions.RUNTIME_DEFAULT
        project.dependencies.add("implementation", "org.authora:authora-android:$runtimeVersion")

        when (val providerType = AuthoraTomlManager.getString(tomlFile, "auth.provider", "type")) {
            "firebase" -> applyFirebase(project)
            "supabase" -> applySupabase(project)
            null -> project.logger.lifecycle(
                "[Authora] No provider configured yet. Run 'authoraAddFirebase' or 'authoraAddSupabase'."
            )
            else -> throw AuthoraConfigException(
                "Unknown provider '$providerType' in Authora.toml.\nUse 'firebase' or 'supabase'."
            )
        }

        val syncTask = project.tasks.register("authoraSyncToml") {
            group = "authora"
            description = "Copies Authora.toml into app assets for runtime access"
            doLast {
                val assetsDir = project.file("src/main/assets")
                assetsDir.mkdirs()
                project.file("Authora.toml").copyTo(File(assetsDir, "Authora.toml"), overwrite = true)
            }
        }
        project.tasks.matching { it.name == "preBuild" }.configureEach {
            dependsOn(syncTask)
        }
    }

    private fun applyFirebase(project: Project) {
        project.pluginManager.apply(GoogleServicesPlugin::class.java)
        project.dependencies.add(
            "implementation",
            project.dependencies.platform("com.google.firebase:firebase-bom:${AuthoraVersions.FIREBASE_BOM}")
        )
        project.dependencies.add("implementation", "com.google.firebase:firebase-auth")
    }

    private fun applySupabase(project: Project) {
        project.dependencies.add(
            "implementation",
            project.dependencies.platform("io.github.jan-tennert.supabase:bom:${AuthoraVersions.SUPABASE_BOM}")
        )
        project.dependencies.add("implementation", "io.github.jan-tennert.supabase:auth-kt")
        project.dependencies.add("implementation", "io.ktor:ktor-client-okhttp:${AuthoraVersions.KTOR}")
    }
}