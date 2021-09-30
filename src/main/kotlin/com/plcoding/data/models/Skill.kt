package com.plcoding.data.models

import com.plcoding.data.responses.SkillResponse
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Skill(
    @BsonId
    val id: String = ObjectId().toString(),
    val name: String,
    val imageUrl: String
) {
    fun toSkillResponse(): SkillResponse {
        return SkillResponse(
            name = name,
            imageUrl = imageUrl
        )
    }
}
