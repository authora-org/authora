package com.authora.gradle.util

import com.authora.gradle.AuthoraConfigException
import org.gradle.api.Project
import java.io.File
import java.util.Properties

object AdbLocator {
    fun locate(project: Project): String {
        listOf(System.getenv("ANDROID_HOME"), System.getenv("ANDROID_SDK_ROOT"))
            .filterNotNull()
            .forEach { home ->
                val adb = File(home, "platform-tools/adb")
                if (adb.exists()) return adb.absolutePath
            }

        val localProps = File(project.rootDir, "local.properties")
        if (localProps.exists()) {
            val props = Properties()
            localProps.inputStream().use { props.load(it) }
            props.getProperty("sdk.dir")?.let { dir ->
                val adb = File(dir, "platform-tools/adb")
                if (adb.exists()) return adb.absolutePath
            }
        }

        throw AuthoraConfigException(
            "Authora could not find adb.\nSet ANDROID_HOME/ANDROID_SDK_ROOT, or add sdk.dir in local.properties."
        )
    }
}