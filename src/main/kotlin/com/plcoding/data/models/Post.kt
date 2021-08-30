package com.plcoding.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Post(
    val imageUrl: String,
    val userId: String,
    val timestamp: Long,
    val description: String,
    @BsonId
    val id: String = ObjectId().toString(),
)
