package com.pocketherbarium.notes.domain.usecase

import com.pocketherbarium.notes.domain.model.Plant
import com.pocketherbarium.notes.domain.repository.PlantRepository

class GetPlantsUseCase(private val repository: PlantRepository) {

    operator fun invoke(): List<Plant> = repository.plants().sortedByDescending { it.createdAt }

    fun byId(id: Long): Plant? = repository.find(id)

    fun readFailed(): Boolean = repository.lastReadFailed()
}
