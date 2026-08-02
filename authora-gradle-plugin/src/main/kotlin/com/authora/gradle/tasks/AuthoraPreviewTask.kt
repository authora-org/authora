package com.authora.gradle.tasks

import com.authora.gradle.AuthoraConfigException
import com.authora.gradle.util.AdbLocator
import com.authora.gradle.util.AndroidProjectInspector
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class AuthoraPreviewTask : DefaultTask() {

    init {
        group = "authora"
        description = "Previews the Authora UI without running the full app"
        project.tasks.findByName("installDebug")?.let { dependsOn(it) }
    }

    @TaskAction
    fun run() {
        val applicationId = AndroidProjectInspector.findApplicationId(project)
            ?: throw AuthoraConfigException("Authora could not find an applicationId. Make sure the Android plugin is applied before Authora.")

        val adb = AdbLocator.locate(project)
        val component = "$applicationId/com.authora.ui.preview.AuthoraPreviewActivity"

        val process = ProcessBuilder(adb, "shell", "am", "start", "-n", component)
            .redirectErrorStream(true)
            .start()
        val output = process.inputStream.bufferedReader().readText()
        val exitCode = process.waitFor()

        if (exitCode != 0 || output.contains("Error", ignoreCase = true)) {
            throw AuthoraConfigException(
                "Authora Preview failed to launch.\nMake sure a device/emulator is connected and the app is installed.\nDetails: ${output.trim()}"
            )
        }

        println("✔ Authora Preview launched on device.")
    }
}