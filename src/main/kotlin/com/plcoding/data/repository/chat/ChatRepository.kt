package com.plcoding.data.repository.chat

import com.plcoding.data.models.Chat
import com.plcoding.data.models.Message

interface ChatRepository {

    suspend fun getMessagesForChat(chatId: String, page: Int, pageSize: Int): List<Message>

    suspend fun getChatsForUser(ownUserId: String): List<Chat>

    suspend fun doesChatBelongToUser(chatId: String, userId: String): Boolean

    suspend fun insertMessage(message: Message)

    suspend fun insertChat(userId1: String, userId2: String, messageId: String)

    suspend fun doesChatByUsersExist(userId1: String, userId2: String): Boolean

    suspend fun updateLastMessageIdForChat(chatId: String, lastMessageId: String)
}