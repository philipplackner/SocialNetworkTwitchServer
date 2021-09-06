package com.plcoding.service

import com.plcoding.data.repository.likes.LikeRepository
import com.plcoding.data.util.ParentType
import org.litote.kmongo.MongoOperator

class LikeService(
    private val repository: LikeRepository
) {

    suspend fun likeParent(userId: String, parentId: String, parentType: Int): Boolean {
        return repository.likeParent(userId, parentId, parentType)
    }

    suspend fun unlikeParent(userId: String, parentId: String): Boolean {
        return repository.unlikeParent(userId, parentId)
    }

    suspend fun deleteLikesForParent(parentId: String) {
        repository.deleteLikesForParent(parentId)
    }
}