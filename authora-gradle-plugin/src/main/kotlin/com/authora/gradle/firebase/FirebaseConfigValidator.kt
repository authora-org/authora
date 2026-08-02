package com.authora.gradle.firebase

import org.json.JSONObject

object FirebaseConfigValidator {

    sealed class Result {
        data class Success(val projectId: String) : Result()
        data class Failure(val reason: String) : Result()
    }

    fun validate(jsonText: String, expectedApplicationId: String?): Result {
        val json = try {
            JSONObject(jsonText)
        } catch (e: Exception) {
            return Result.Failure("File is not valid JSON: ${e.message}")
        }

        val projectInfo = json.optJSONObject("project_info")
            ?: return Result.Failure("Field 'project_info' not found in google-services.json.")
        val projectId = projectInfo.optString("project_id", "")
        if (projectId.isBlank()) return Result.Failure("Field 'project_info.project_id' is empty.")

        val clients = json.optJSONArray("client")
        if (clients == null || clients.length() == 0) {
            return Result.Failure("Field 'client' is empty. This file has no Android app configuration.")
        }

        val firstClient = clients.getJSONObject(0)
        val clientInfo = firstClient.optJSONObject("client_info")
            ?: return Result.Failure("Field 'client_info' not found on the first client.")
        val androidInfo = clientInfo.optJSONObject("android_client_info")
            ?: return Result.Failure("Field 'android_client_info' not found.")
        val packageName = androidInfo.optString("package_name", "")
        if (packageName.isBlank()) return Result.Failure("Field 'package_name' is empty.")

        if (expectedApplicationId != null && expectedApplicationId != packageName) {
            return Result.Failure(
                "Module applicationId ('$expectedApplicationId') does not match package_name in google-services.json ('$packageName')."
            )
        }

        val apiKeys = firstClient.optJSONArray("api_key")
        if (apiKeys == null || apiKeys.length() == 0) return Result.Failure("Field 'api_key' is empty.")
        val currentKey = apiKeys.getJSONObject(0).optString("current_key", "")
        if (currentKey.isBlank()) return Result.Failure("Field 'current_key' is empty in api_key.")

        return Result.Success(projectId)
    }
}