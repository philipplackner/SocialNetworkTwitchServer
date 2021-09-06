package com.plcoding.service

import com.plcoding.data.repository.follow.FollowRepository
import com.plcoding.data.requests.FollowUpdateRequest

class FollowService(
    private val followRepository: FollowRepository
) {

    suspend fun followUserIfExists(request: FollowUpdateRequest, followingUserId: String): Boolean {
        return followRepository.followUserIfExists(
            followingUserId,
            request.followedUserId
        )
    }

    suspend fun unfollowUserIfExists(request: FollowUpdateRequest, followingUserId: String): Boolean {
        return followRepository.unfollowUserIfExists(
            followingUserId,
            request.followedUserId
        )
    }
}