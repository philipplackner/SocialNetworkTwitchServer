package com.plcoding.routes

import com.plcoding.data.requests.CreateCommentRequest
import com.plcoding.data.requests.CreatePostRequest
import com.plcoding.data.requests.DeleteCommentRequest
import com.plcoding.data.requests.DeletePostRequest
import com.plcoding.data.responses.BasicApiResponse
import com.plcoding.service.CommentService
import com.plcoding.service.LikeService
import com.plcoding.service.UserService
import com.plcoding.util.ApiResponseMessages
import com.plcoding.util.QueryParams
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createComment(
    commentService: CommentService,
    userService: UserService
) {
    authenticate {
        post("/api/comment/create") {
            val request = call.receiveOrNull<CreateCommentRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            ifEmailBelongsToUser(
                userId = request.userId,
                validateEmail = userService::doesEmailBelongToUserId
            ) {
                when(commentService.createComment(request)) {
                    is CommentService.ValidationEvent.ErrorFieldEmpty -> {
                        call.respond(
                            HttpStatusCode.OK,
                            BasicApiResponse(
                                successful = false,
                                message = ApiResponseMessages.FIELDS_BLANK
                            )
                        )
                    }
                    is CommentService.ValidationEvent.ErrorCommentTooLong -> {
                        call.respond(
                            HttpStatusCode.OK,
                            BasicApiResponse(
                                successful = false,
                                message = ApiResponseMessages.COMMENT_TOO_LONG
                            )
                        )
                    }
                    is CommentService.ValidationEvent.Success -> {
                        call.respond(
                            HttpStatusCode.OK,
                            BasicApiResponse(
                                successful = true,
                            )
                        )
                    }
                }
            }
        }
    }
}

fun Route.getCommentsForPost(
    commentService: CommentService,
) {
    authenticate {
        get("/api/comment/get") {
            val postId = call.parameters[QueryParams.PARAM_POST_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val comments = commentService.getCommentsForPost(postId)
            call.respond(HttpStatusCode.OK, comments)
        }
    }
}

fun Route.deleteComment(
    commentService: CommentService,
    userService: UserService,
    likeService: LikeService
) {
    authenticate {
        delete("/api/comment/delete") {
            val request = call.receiveOrNull<DeleteCommentRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            ifEmailBelongsToUser(
                userId = request.userId,
                validateEmail = userService::doesEmailBelongToUserId
            ) {
                val deleted = commentService.deleteComment(request.commentId)
                if(deleted) {
                    likeService.deleteLikesForParent(request.commentId)
                    call.respond(HttpStatusCode.OK, BasicApiResponse(successful = true))
                } else {
                    call.respond(HttpStatusCode.NotFound, BasicApiResponse(successful = false))
                }
            }
        }
    }
}