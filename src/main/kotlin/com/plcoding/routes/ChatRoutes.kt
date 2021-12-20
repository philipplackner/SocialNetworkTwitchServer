package com.plcoding.routes

import com.google.gson.Gson
import com.plcoding.data.websocket.WsClientMessage
import com.plcoding.data.websocket.WsServerMessage
import com.plcoding.service.chat.ChatController
import com.plcoding.service.chat.ChatService
import com.plcoding.service.chat.ChatSession
import com.plcoding.util.Constants
import com.plcoding.util.QueryParams
import com.plcoding.util.WebSocketObject
import com.plcoding.util.fromJsonOrNull
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import org.koin.java.KoinJavaComponent.inject

fun Route.getMessagesForChat(chatService: ChatService) {
    authenticate {
        get("/api/chat/messages") {
            val chatId = call.parameters[QueryParams.PARAM_CHAT_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull() ?: Constants.DEFAULT_PAGE_SIZE

            if (!chatService.doesChatBelongToUser(chatId, call.userId)) {
                call.respond(HttpStatusCode.Forbidden)
                return@get
            }

            val messages = chatService.getMessagesForChat(chatId, page, pageSize)
            call.respond(HttpStatusCode.OK, messages)
        }
    }
}

fun Route.getChatsForUser(chatService: ChatService) {
    authenticate {
        get("/api/chats") {
            val chats = chatService.getChatsForUser(call.userId)
            call.respond(HttpStatusCode.OK, chats)
        }
    }
}

fun Route.chatWebSocket(chatController: ChatController) {
    authenticate {
        webSocket("/api/chat/websocket") {
            println("Connecting via web socket")
            chatController.onJoin(call.userId, this)
            try {
                incoming.consumeEach { frame ->
                    kotlin.run {
                        when (frame) {
                            is Frame.Text -> {
                                val frameText = frame.readText()
                                val delimiterIndex = frameText.indexOf("#")
                                if(delimiterIndex == -1) {
                                    println("No delimiter found")
                                    return@run
                                }
                                val type = frameText.substring(0, delimiterIndex).toIntOrNull()
                                if(type == null) {
                                    println("Invalid format")
                                    return@run
                                }
                                val json = frameText.substring(delimiterIndex + 1, frameText.length)
                                handleWebSocket(call.userId, chatController, type, frameText, json)
                            }
                            else -> Unit
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                println("Disconnecting ${call.userId}")
                chatController.onDisconnect(call.userId)
            }
        }
    }
}

suspend fun handleWebSocket(
    ownUserId: String,
    chatController: ChatController,
    type: Int,
    frameText: String,
    json: String
) {
    val gson by inject<Gson>(Gson::class.java)
    when(type) {
        WebSocketObject.MESSAGE.ordinal -> {
            val message = gson.fromJsonOrNull(json, WsClientMessage::class.java) ?: return
            println("Received message $message from $ownUserId")
            chatController.sendMessage(ownUserId, gson, message)
        }
    }
}