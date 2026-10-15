package com.pocketherbarium.notes.domain.usecase

import com.pocketherbarium.notes.domain.model.Plant
import com.pocketherbarium.notes.domain.repository.CollectionRepository
import com.pocketherbarium.notes.domain.repository.PlantRepository

class SavePlantUseCase(
    private val plantRepository: PlantRepository,
    private val collectionRepository: CollectionRepository
) {

    operator fun invoke(plant: Plant): Plant {
        val target = plant.collectionName.ifBlank { collectionRepository.activeName() }
        return plantRepository.save(plant.copy(collectionName = target))
    }
}
