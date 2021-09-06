package com.plcoding.data.util

sealed class ActivityType(val type: Int) {
    object LikedPost : ActivityType(0)
    object LikedComment : ActivityType(1)
    object CommentedOnPost : ActivityType(2)
    object FollowedUser : ActivityType(3)
}
