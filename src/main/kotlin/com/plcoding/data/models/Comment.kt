package com.plcoding.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Comment(
    val comment: String,
    val username: String,
    val profileImageUrl: String,
    val userId: String,
    val postId: String,
    val timestamp: Long,
    val likeCount: Int,
    @BsonId
    val id: String = ObjectId().toString(),
)
