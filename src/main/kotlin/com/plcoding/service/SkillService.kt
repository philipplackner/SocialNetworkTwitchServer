package com.plcoding.service

import com.plcoding.data.models.Skill
import com.plcoding.data.repository.skill.SkillRepository

class SkillService(
    private val repository: SkillRepository
) {

    suspend fun getSkills(): List<Skill> {
        return repository.getSkills()
    }
}