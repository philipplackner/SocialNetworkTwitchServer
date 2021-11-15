package com.plcoding.plugins

import com.plcoding.service.chat.ChatSession
import io.ktor.application.*
import io.ktor.sessions.*

fun Application.configureSessions() {
    install(Sessions) {
        cookie<ChatSession>("SESSION")
    }
}