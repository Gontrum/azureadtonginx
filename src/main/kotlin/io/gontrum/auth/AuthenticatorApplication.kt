package io.gontrum.auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class AuthenticatorApplication

fun main(args: Array<String>) {
    runApplication<AuthenticatorApplication>(*args)
}
