package com.plcoding.data.repository.chat

import com.plcoding.data.models.Chat
import com.plcoding.data.models.Message
import com.plcoding.data.models.User
import com.plcoding.data.responses.ChatDto
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase

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
            .ascendingSort(Message::timestamp)
            .toList()
    }

    override suspend fun getChatsForUser(ownUserId: String): List<ChatDto> {
        return chats.find(Chat::userIds contains ownUserId)
            .descendingSort(Chat::timestamp)
            .toList()
            .map { chat ->
                val otherUserId = chat.userIds.find { it != ownUserId }
                val user = users.findOneById(otherUserId ?: "")
                val message = messages.findOneById(chat.lastMessageId)
                ChatDto(
                    chatId = chat.id,
                    remoteUserId = user?.id,
                    remoteUsername = user?.username,
                    remoteUserProfilePictureUrl = user?.profileImageUrl,
                    lastMessage = message?.text,
                    timestamp = message?.timestamp
                )
            }
    }

    override suspend fun doesChatBelongToUser(chatId: String, userId: String): Boolean {
        return chats.findOneById(chatId)?.userIds?.any { it == userId } == true
    }

    override suspend fun insertMessage(message: Message) {
        messages.insertOne(message)
    }

    override suspend fun insertChat(userId1: String, userId2: String, messageId: String) {
        val chat = Chat(
            userIds = listOf(
                userId1,
                userId2
            ),
            lastMessageId = messageId,
            timestamp = System.currentTimeMillis()
        )
        val chatId = chats.insertOne(chat).insertedId?.asObjectId().toString()
        messages.updateOneById(messageId, setValue(Message::chatId, chatId))
    }

    override suspend fun doesChatByUsersExist(userId1: String, userId2: String): Boolean {
        return chats.find(
            and(
                Chat::userIds contains userId1,
                Chat::userIds contains userId2
            )
        ).first() != null
    }

    override suspend fun updateLastMessageIdForChat(chatId: String, lastMessageId: String) {
        chats.updateOneById(chatId, setValue(Chat::lastMessageId, lastMessageId))
    }
}