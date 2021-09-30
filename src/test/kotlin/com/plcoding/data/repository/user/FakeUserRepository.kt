package com.plcoding.data.repository.user

import com.plcoding.data.models.User
import com.plcoding.data.requests.UpdateProfileRequest

class FakeUserRepository : UserRepository {

    val users = mutableListOf<User>()

    override suspend fun createUser(user: User) {
        users.add(user)
    }

    override suspend fun getUserById(id: String): User? {
        return users.find { it.id == id }
    }

    override suspend fun getUserByEmail(email: String): User? {
        return users.find { it.email == email }
    }

    override suspend fun updateUser(
        userId: String,
        profileImageUrl: String?,
        bannerUrl: String?,
        updateProfileRequest: UpdateProfileRequest
    ): Boolean {

    }

    override suspend fun doesPasswordForUserMatch(email: String, enteredPassword: String): Boolean {
        val user = getUserByEmail(email)
        return user?.password == enteredPassword
    }

    override suspend fun doesEmailBelongToUserId(email: String, userId: String): Boolean {

    }

    override suspend fun searchForUsers(query: String): List<User> {

    }

    override suspend fun getUsers(userIds: List<String>): List<User> {

    }
}