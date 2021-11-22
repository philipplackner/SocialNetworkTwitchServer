package com.plcoding.data.websocket

import com.plcoding.data.models.Message
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class WsMessage(
    val fromId: String,
    val toId: String,
    val text: String,
    val timestamp: Long,
    val chatId: String?,
) {
    fun toMessage(): Message {
        return Message(
            fromId = fromId,
            toId = toId,
            text = text,
            timestamp = timestamp,
            chatId = chatId
        )
    }
}
