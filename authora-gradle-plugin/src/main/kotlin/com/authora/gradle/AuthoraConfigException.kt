package com.authora.gradle

import org.gradle.api.GradleException

class AuthoraConfigException(message: String) : GradleException(message) {
    override fun fillInStackTrace(): Throwable = this
}