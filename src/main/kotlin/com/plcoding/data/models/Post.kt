package com.plcoding.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Post(
    val imageUrl: String,
    val userId: String,
    val timestamp: Long,
    val description: String,
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    @BsonId
    val id: String = ObjectId().toString(),
)
