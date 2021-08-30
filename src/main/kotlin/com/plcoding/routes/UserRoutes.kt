package com.plcoding.routes

import com.plcoding.data.models.User
import com.plcoding.data.repository.user.UserRepository
import com.plcoding.data.requests.CreateAccountRequest
import com.plcoding.data.requests.LoginRequest
import com.plcoding.data.responses.BasicApiResponse
import com.plcoding.util.ApiResponseMessages.FIELDS_BLANK
import com.plcoding.util.ApiResponseMessages.INVALID_CREDENTIALS
import com.plcoding.util.ApiResponseMessages.USER_ALREADY_EXISTS
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createUserRoute(userRepository: UserRepository) {
    post("/api/user/create") {
        val request = call.receiveOrNull<CreateAccountRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val userExists = userRepository.getUserByEmail(request.email) != null
        if (userExists) {
            call.respond(
                BasicApiResponse(
                    successful = false,
                    message = USER_ALREADY_EXISTS
                )
            )
            return@post
        }
        if (request.email.isBlank() || request.password.isBlank() || request.username.isBlank()) {
            call.respond(
                BasicApiResponse(
                    successful = false,
                    message = FIELDS_BLANK
                )
            )
            return@post
        }
        userRepository.createUser(
            User(
                email = request.email,
                username = request.username,
                password = request.password,
                profileImageUrl = "",
                bio = "",
                gitHubUrl = null,
                instagramUrl = null,
                linkedInUrl = null
            )
        )
        call.respond(
            BasicApiResponse(successful = true)
        )
    }
}

fun Route.loginUser(userRepository: UserRepository) {
    post("/api/user/login") {
        val request = call.receiveOrNull<LoginRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        if (request.email.isBlank() || request.password.isBlank()) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val isCorrectPassword = userRepository.doesPasswordForUserMatch(
            email = request.email,
            enteredPassword = request.password
        )
        if(isCorrectPassword) {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true
                )
            )
        } else {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = false,
                    message = INVALID_CREDENTIALS
                )
            )
        }
    }
}