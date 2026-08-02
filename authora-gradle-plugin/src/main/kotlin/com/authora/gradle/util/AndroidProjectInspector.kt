package com.authora.gradle.util

import org.gradle.api.Project

object AndroidProjectInspector {
    fun findApplicationId(project: Project): String? {
        val androidExt = project.extensions.findByName("android") ?: return null
        return try {
            val defaultConfig = androidExt.javaClass.getMethod("getDefaultConfig").invoke(androidExt)
            defaultConfig.javaClass.getMethod("getApplicationId").invoke(defaultConfig) as? String
        } catch (e: Exception) {
            null
        }
    }
}