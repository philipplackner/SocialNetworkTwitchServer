package com.plcoding.routes

import com.plcoding.data.models.Post
import com.plcoding.data.repository.post.PostRepository
import com.plcoding.data.requests.CreateAccountRequest
import com.plcoding.data.requests.CreatePostRequest
import com.plcoding.data.responses.BasicApiResponse
import com.plcoding.service.PostService
import com.plcoding.util.ApiResponseMessages
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createPostRoute(postService: PostService) {
    post("/api/post/create") {
        val request = call.receiveOrNull<CreatePostRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val didUserExist = postService.createPostIfUserExists(request)
        if (didUserExist) {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = false,
                    message = ApiResponseMessages.USER_NOT_FOUND
                )
            )
        } else {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true,
                )
            )
        }
    }
}