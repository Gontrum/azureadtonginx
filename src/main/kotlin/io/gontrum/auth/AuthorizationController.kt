package io.gontrum.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthorizationController @Autowired constructor(private val env: Environment) {

    @Autowired
    @RequestMapping("/auth")
    fun authorize(): String {
        return "Authorized"
    }

}
