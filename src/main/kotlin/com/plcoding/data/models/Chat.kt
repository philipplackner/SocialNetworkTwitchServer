package com.plcoding.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Chat(
    val userIds: List<String>,
    val lastMessageId: String,
    val timestamp: Long,
    @BsonId
    val id: String = ObjectId().toString(),
)
