package com.plcoding.service.chat

import com.plcoding.data.models.Message
import com.plcoding.data.repository.chat.ChatRepository
import io.ktor.http.cio.websocket.*
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

    suspend fun sendMessage(message: Message) {
        onlineUsers[message.fromId]?.send(Frame.Text(message.text))
        repository.insertMessage(message)
    }
}