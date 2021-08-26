package com.plcoding.routes

import com.plcoding.controller.user.UserController
import com.plcoding.data.models.User
import com.plcoding.data.requests.CreateAccountRequest
import com.plcoding.data.responses.BasicApiResponse
import com.plcoding.util.ApiResponseMessages
import com.plcoding.util.ApiResponseMessages.FIELDS_BLANK
import com.plcoding.util.ApiResponseMessages.USER_ALREADY_EXISTS
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Route.userRoutes() {
    val userController: UserController by inject()
    route("/api/user/create") {
        post {
            val request = call.receiveOrNull<CreateAccountRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val userExists = userController.getUserByEmail(request.email) != null
            if(userExists) {
                call.respond(
                    BasicApiResponse(
                        successful = false,
                        message = USER_ALREADY_EXISTS
                    )
                )
                return@post
            }
            if(request.email.isBlank() || request.password.isBlank() || request.username.isBlank()) {
                call.respond(
                    BasicApiResponse(
                        successful = false,
                        message = FIELDS_BLANK
                    )
                )
                return@post
            }
            userController.createUser(
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
}