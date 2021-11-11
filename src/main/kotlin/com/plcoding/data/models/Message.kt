package com.plcoding.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Message(
    val fromId: String,
    val toId: String,
    val text: String,
    val timestamp: Long,
    val chatId: String,
    @BsonId
    val id: String = ObjectId().toString(),
)
