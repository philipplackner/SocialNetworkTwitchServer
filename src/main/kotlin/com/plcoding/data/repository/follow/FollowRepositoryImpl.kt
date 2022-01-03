package com.plcoding.data.repository.follow

import com.plcoding.data.models.Following
import com.plcoding.data.models.User
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.inc

class FollowRepositoryImpl(
    db: CoroutineDatabase
) : FollowRepository {

    private val following = db.getCollection<Following>()
    private val users = db.getCollection<User>()

    override suspend fun followUserIfExists(
        followingUserId: String,
        followedUserId: String
    ): Boolean {
        val doesFollowingUserExist = users.findOneById(followingUserId) != null
        val doesFollowedUserExist = users.findOneById(followedUserId) != null
        if(!doesFollowingUserExist || !doesFollowedUserExist) {
            return false
        }
        users.updateOneById(
            followingUserId,
            inc(User::followingCount, 1)
        )
        users.updateOneById(
            followedUserId,
            inc(User::followerCount, 1)
        )
        following.insertOne(
            Following(followingUserId, followedUserId)
        )
        return true
    }

    override suspend fun unfollowUserIfExists(followingUserId: String, followedUserId: String): Boolean {
        val deleteResult = following.deleteOne(
            and(
                Following::followingUserId eq followingUserId,
                Following::followedUserId eq followedUserId,
            )
        )
        if(deleteResult.deletedCount > 0) {
            users.updateOneById(
                followingUserId,
                inc(User::followingCount, -1)
            )
            users.updateOneById(
                followedUserId,
                inc(User::followerCount, -1)
            )
        }
        return deleteResult.deletedCount > 0
    }

    override suspend fun doesUserFollow(followingUserId: String, followedUserId: String): Boolean {
        return following.findOne(
            and(
                Following::followingUserId eq followingUserId,
                Following::followedUserId eq followedUserId
            )
        ) != null
    }

    override suspend fun getFollowsByUser(userId: String): List<Following> {
        return following.find(Following::followingUserId eq userId).toList()
    }
}