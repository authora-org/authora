package com.authora.core.config

class AuthoraConfigException(message: String) : RuntimeException(message) {
    override fun fillInStackTrace(): Throwable = this
}