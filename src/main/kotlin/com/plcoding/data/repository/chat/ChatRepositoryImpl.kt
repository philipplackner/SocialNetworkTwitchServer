package com.plcoding.data.repository.chat

import com.plcoding.data.models.Chat
import com.plcoding.data.models.Message
import com.plcoding.data.models.SimpleUser
import com.plcoding.data.models.User
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class ChatRepositoryImpl(
    db: CoroutineDatabase
): ChatRepository {

    private val chats = db.getCollection<Chat>()
    private val users = db.getCollection<User>()
    private val messages = db.getCollection<Message>()

    override suspend fun getMessagesForChat(chatId: String, page: Int, pageSize: Int): List<Message> {
        return messages.find(Message::chatId eq chatId)
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Message::timestamp)
            .toList()
    }

    override suspend fun getChatsForUser(ownUserId: String): List<Chat> {
        val user = users.findOneById(ownUserId) ?: return emptyList()
        val simpleUser = SimpleUser(
            id = user.id,
            profilePictureUrl = user.profileImageUrl,
            username = user.username
        )
        return chats.find(Chat::users contains simpleUser)
            .descendingSort(Chat::timestamp)
            .toList()
    }

    override suspend fun doesChatBelongToUser(chatId: String, userId: String): Boolean {
        return chats.findOneById(chatId)?.users?.any { it.id == userId } == true
    }

    override suspend fun insertMessage(message: Message) {
        messages.insertOne(message)
    }
}