package com.authora.gradle.util

object ConsolePrompter {
    private val reader = System.`in`.bufferedReader()

    fun ask(label: String): String {
        println(label)
        print("«» ")
        System.out.flush()
        return reader.readLine()?.trim().orEmpty()
    }

    fun askMultiline(label: String): String {
        println(label)
        println("(paste the content, then press Enter twice to finish)")
        val sb = StringBuilder()
        while (true) {
            val line = reader.readLine() ?: break
            if (line.isBlank() && sb.isNotBlank()) break
            if (line.isNotBlank()) sb.appendLine(line)
        }
        return sb.toString().trim()
    }
}