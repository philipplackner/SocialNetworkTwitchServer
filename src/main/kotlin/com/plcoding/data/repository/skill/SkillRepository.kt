package com.plcoding.data.repository.skill

import com.plcoding.data.models.Skill

interface SkillRepository {

    suspend fun getSkills(): List<Skill>
}