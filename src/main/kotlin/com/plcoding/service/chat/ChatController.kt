package com.plcoding.service.chat

import com.plcoding.data.models.Chat
import com.plcoding.data.models.Message
import com.plcoding.data.models.SimpleUser
import com.plcoding.data.repository.chat.ChatRepository
import com.plcoding.data.websocket.WsMessage
import io.ktor.http.cio.websocket.*
import org.litote.kmongo.util.idValue
import java.util.concurrent.ConcurrentHashMap

class ChatController(
    private val repository: ChatRepository
) {

    private val onlineUsers = ConcurrentHashMap<String, WebSocketSession>()

    fun onJoin(chatSession: ChatSession, socket: WebSocketSession) {
        onlineUsers[chatSession.userId] = socket
    }

    fun onDisconnect(userId: String) {
        if(onlineUsers.containsKey(userId)) {
            onlineUsers.remove(userId)
        }
    }

    suspend fun sendMessage(json: String, message: WsMessage) {
        onlineUsers[message.fromId]?.send(Frame.Text(json))
        onlineUsers[message.toId]?.send(Frame.Text(json))
        val messageEntity = message.toMessage()
        repository.insertMessage(messageEntity)
        if(!repository.doesChatByUsersExist(message.fromId, message.toId)) {
            repository.insertChat(message.fromId, message.toId, messageEntity.id)
        } else {
            message.chatId?.let {
                repository.updateLastMessageIdForChat(message.chatId, messageEntity.id)
            }
        }
    }
}