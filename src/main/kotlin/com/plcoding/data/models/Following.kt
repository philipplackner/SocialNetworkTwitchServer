package com.plcoding.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Following(
    @BsonId
    val id: String = ObjectId().toString(),
    val followingUserId: String,
    val followedUserId: String
)
