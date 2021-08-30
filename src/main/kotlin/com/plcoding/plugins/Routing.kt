package com.plcoding.plugins

import com.plcoding.data.repository.follow.FollowRepository
import com.plcoding.data.repository.post.PostRepository
import com.plcoding.data.repository.user.UserRepository
import com.plcoding.routes.*
import io.ktor.routing.*
import io.ktor.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userRepository: UserRepository by inject()
    val followRepository: FollowRepository by inject()
    val postRepository: PostRepository by inject()
    routing {
        // User routes
        createUserRoute(userRepository)
        loginUser(userRepository)

        // Following routes
        followUser(followRepository)
        unfollowUser(followRepository)

        // Post routes
        createPostRoute(postRepository)
    }
}
