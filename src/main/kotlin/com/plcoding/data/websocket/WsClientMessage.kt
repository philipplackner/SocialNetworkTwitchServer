package com.plcoding.data.websocket

import com.plcoding.data.models.Message
import org.litote.kmongo.MongoOperator

data class WsClientMessage(
    val toId: String,
    val text: String,
    val chatId: String?,
) {
    fun toMessage(fromId: String): Message {
        return Message(
            fromId = fromId,
            toId = toId,
            text = text,
            timestamp = System.currentTimeMillis(),
            chatId = chatId
        )
    }
}
