package com.pocketherbarium.notes.domain.usecase

import com.pocketherbarium.notes.domain.model.Plant
import com.pocketherbarium.notes.domain.repository.CollectionRepository
import com.pocketherbarium.notes.domain.repository.PlantRepository

class ResetLibraryUseCase(
    private val plantRepository: PlantRepository,
    private val collectionRepository: CollectionRepository
) {

    operator fun invoke(): List<Plant> {
        collectionRepository.reset()
        return plantRepository.reset()
    }
}
