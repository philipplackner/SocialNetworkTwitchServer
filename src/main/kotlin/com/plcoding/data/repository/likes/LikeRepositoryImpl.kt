package com.plcoding.data.repository.likes

import com.plcoding.data.models.Like
import com.plcoding.data.models.Post
import com.plcoding.data.models.User
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class LikeRepositoryImpl(
    db: CoroutineDatabase
) : LikeRepository {

    private val likes = db.getCollection<Like>()
    private val users = db.getCollection<User>()

    override suspend fun likeParent(userId: String, parentId: String, parentType: Int): Boolean {
        val doesUserExist = users.findOneById(userId) != null
        return if(doesUserExist) {
            likes.insertOne(Like(userId, parentId, parentType, System.currentTimeMillis()))
            true
        } else {
            false
        }
    }

    override suspend fun unlikeParent(userId: String, parentId: String): Boolean {
        val doesUserExist = users.findOneById(userId) != null
        return if(doesUserExist) {
            likes.deleteOne(
                and(
                    Like::userId eq userId,
                    Like::parentId eq parentId
                )
            )
            true
        } else {
            false
        }
    }

    override suspend fun deleteLikesForParent(parentId: String) {
        likes.deleteMany(Like::parentId eq parentId)
    }

    override suspend fun getLikesForParent(parentId: String, page: Int, pageSize: Int): List<Like> {
        return likes
            .find(Like::parentId eq parentId)
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Like::timestamp)
            .toList()
    }
}