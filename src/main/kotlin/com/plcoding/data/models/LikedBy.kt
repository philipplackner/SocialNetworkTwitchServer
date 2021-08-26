package com.plcoding.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class LikedBy(
    @BsonId
    val id: String = ObjectId().toString(),
    val userId: String,
    val parentId: String
)
