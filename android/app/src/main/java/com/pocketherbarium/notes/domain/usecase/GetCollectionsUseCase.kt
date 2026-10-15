package com.pocketherbarium.notes.domain.usecase

import com.pocketherbarium.notes.domain.model.PlantCollection
import com.pocketherbarium.notes.domain.model.TraitCatalog
import com.pocketherbarium.notes.domain.repository.CollectionRepository
import com.pocketherbarium.notes.domain.repository.PlantRepository

class GetCollectionsUseCase(
    private val plantRepository: PlantRepository,
    private val collectionRepository: CollectionRepository
) {

    operator fun invoke(): List<PlantCollection> {
        val plants = plantRepository.plants()
        val active = collectionRepository.activeName()
        return collectionRepository.names().map { name ->
            val members = plants.filter { it.collectionName == name }
            val sprite = members.firstOrNull()?.let { TraitCatalog.spriteKeyForHabitat(it.habitatId) }
                ?: TraitCatalog.spriteKeyForShape(TraitCatalog.DEFAULT_SHAPE)
            PlantCollection(name, members.size, sprite, name == active)
        }
    }

    fun countIn(name: String): Int =
        plantRepository.plants().count { it.collectionName == name }
}
