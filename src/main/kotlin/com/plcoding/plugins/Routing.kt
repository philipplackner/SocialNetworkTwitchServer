package com.plcoding.plugins

import com.plcoding.data.repository.follow.FollowRepository
import com.plcoding.data.repository.user.UserRepository
import com.plcoding.routes.createUserRoute
import com.plcoding.routes.followUser
import com.plcoding.routes.loginUser
import com.plcoding.routes.unfollowUser
import io.ktor.routing.*
import io.ktor.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userRepository: UserRepository by inject()
    val followRepository: FollowRepository by inject()
    routing {
        // User routes
        createUserRoute(userRepository)
        loginUser(userRepository)

        // Following routes
        followUser(followRepository)
        unfollowUser(followRepository)
    }
}
