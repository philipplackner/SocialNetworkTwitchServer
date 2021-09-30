package com.plcoding.data.repository.skill

import com.plcoding.data.models.Skill
import org.litote.kmongo.MongoOperator
import org.litote.kmongo.coroutine.CoroutineDatabase

class SkillRepositoryImpl(
    private val db: CoroutineDatabase
): SkillRepository {

    private val skills = db.getCollection<Skill>()

    override suspend fun getSkills(): List<Skill> {
        return skills.find().toList()
    }
}