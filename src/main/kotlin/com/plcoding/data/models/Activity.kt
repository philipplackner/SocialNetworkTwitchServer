package com.plcoding.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Activity(
    val timestamp: Long,
    val byUserId: String,
    val toUserId: String,
    val type: Int,
    val parentId: String,
    @BsonId
    val id: String = ObjectId().toString(),
)