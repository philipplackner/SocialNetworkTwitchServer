package com.plcoding.plugins

import com.plcoding.data.repository.follow.FollowRepository
import com.plcoding.data.repository.post.PostRepository
import com.plcoding.data.repository.user.UserRepository
import com.plcoding.routes.*
import com.plcoding.service.FollowService
import com.plcoding.service.PostService
import com.plcoding.service.UserService
import io.ktor.routing.*
import io.ktor.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userService: UserService by inject()

    val followService: FollowService by inject()

    val postService: PostService by inject()

    val jwtIssuer = environment.config.property("jwt.domain").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()
    routing {
        // User routes
        createUserRoute(userService)
        loginUser(
            userService = userService,
            jwtIssuer = jwtIssuer,
            jwtAudience = jwtAudience,
            jwtSecret = jwtSecret
        )

        // Following routes
        followUser(followService)
        unfollowUser(followService)

        // Post routes
        createPostRoute(postService, userService)
    }
}
