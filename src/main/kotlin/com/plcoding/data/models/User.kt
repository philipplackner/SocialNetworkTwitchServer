package com.plcoding.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    val email: String,
    val username: String,
    val password: String,
    val profileImageUrl: String,
    val bio: String,
    val gitHubUrl: String?,
    val instagramUrl: String?,
    val linkedInUrl: String?,
    val skills: List<String> = listOf(),
    @BsonId
    val id: String = ObjectId().toString(),
)
