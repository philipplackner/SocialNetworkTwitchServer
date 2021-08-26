package com.plcoding.controller.user

import com.plcoding.data.models.User

interface UserController {

    suspend fun createUser(user: User)

    suspend fun getUserById(id: String): User?

    suspend fun getUserByEmail(email: String): User?

}