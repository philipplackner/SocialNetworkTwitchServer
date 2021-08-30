package com.plcoding.plugins

import com.plcoding.data.repository.user.UserRepository
import com.plcoding.routes.createUserRoute
import com.plcoding.routes.loginUser
import io.ktor.routing.*
import io.ktor.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userRepository: UserRepository by inject()
    routing {
        createUserRoute(userRepository)
        loginUser(userRepository)
    }
}
