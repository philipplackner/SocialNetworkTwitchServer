package com.plcoding.service.chat

import com.google.gson.Gson
import com.plcoding.data.repository.chat.ChatRepository
import com.plcoding.data.websocket.WsClientMessage
import com.plcoding.data.websocket.WsServerMessage
import com.plcoding.util.WebSocketObject
import io.ktor.http.cio.websocket.*
import java.util.concurrent.ConcurrentHashMap

class ChatController(
    private val repository: ChatRepository
) {

    private val onlineUsers = ConcurrentHashMap<String, WebSocketSession>()

    fun onJoin(userId: String, socket: WebSocketSession) {
        onlineUsers[userId] = socket
    }

    fun onDisconnect(userId: String) {
        if(onlineUsers.containsKey(userId)) {
            onlineUsers.remove(userId)
        }
    }

    suspend fun sendMessage(ownUserId: String, gson: Gson, message: WsClientMessage) {
        val messageEntity = message.toMessage(ownUserId)
        val wsServerMessage = WsServerMessage(
            fromId = ownUserId,
            toId = message.toId,
            text = message.text,
            timestamp = System.currentTimeMillis(),
            chatId = message.chatId
        )
        val frameText = gson.toJson(wsServerMessage)
        onlineUsers[ownUserId]?.send(Frame.Text("${WebSocketObject.MESSAGE.ordinal}#$frameText"))
        onlineUsers[message.toId]?.send(Frame.Text("${WebSocketObject.MESSAGE.ordinal}#$frameText"))
        repository.insertMessage(messageEntity)
        if(!repository.doesChatByUsersExist(ownUserId, message.toId)) {
            repository.insertChat(ownUserId, message.toId, messageEntity.id)
        } else {
            message.chatId?.let {
                repository.updateLastMessageIdForChat(message.chatId, messageEntity.id)
            }
        }
    }
}