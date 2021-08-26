package com.plcoding.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    val email: String,
    val username: String,
    val password: String,
    val profileImageUrl: String,
    val bio: String,
    val skills: List<String> = listOf(),
    val gitHubUrl: String?,
    val instagramUrl: String?,
    val linkedInUrl: String?,
    @BsonId
    val id: String = ObjectId().toString(),
)
