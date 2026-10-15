package com.pocketherbarium.notes.data.repository

import com.pocketherbarium.notes.data.local.HerbariumPreferences
import com.pocketherbarium.notes.data.local.PlantSerializer
import com.pocketherbarium.notes.data.sample.SampleData
import com.pocketherbarium.notes.domain.model.Plant
import com.pocketherbarium.notes.domain.repository.PlantRepository

class PlantRepositoryImpl(private val preferences: HerbariumPreferences) : PlantRepository {

    override fun plants(): List<Plant> {
        seedIfNeeded()
        val stored = PlantSerializer.decodeAll(preferences.specimens())
        if (stored.isEmpty() && preferences.lastReadFailed()) return SampleData.plants()
        return stored
    }

    override fun find(id: Long): Plant? = plants().firstOrNull { it.id == id }

    override fun save(plant: Plant): Plant {
        seedIfNeeded()
        val current = PlantSerializer.decodeAll(preferences.specimens()).toMutableList()
        val stamped = if (plant.id > 0L) plant else plant.copy(
            id = nextId(current),
            createdAt = System.currentTimeMillis()
        )
        current.removeAll { it.id == stamped.id }
        current.add(stamped)
        preferences.writeSpecimens(PlantSerializer.encodeAll(current))
        return stamped
    }

    override fun reset(): List<Plant> {
        val fresh = SampleData.plants()
        preferences.writeSpecimens(PlantSerializer.encodeAll(fresh))
        preferences.markSeeded(true)
        return fresh
    }

    override fun lastReadFailed(): Boolean = preferences.lastReadFailed()

    private fun seedIfNeeded() {
        if (preferences.seeded()) return
        preferences.writeSpecimens(PlantSerializer.encodeAll(SampleData.plants()))
        preferences.writeCollections(PlantSerializer.encodeNames(SampleData.collections()))
        preferences.writeActiveCollection(SampleData.COLLECTION_WOODLAND)
        preferences.markSeeded(true)
    }

    private fun nextId(current: List<Plant>): Long {
        val now = System.currentTimeMillis()
        val highest = current.maxOfOrNull { it.id } ?: 0L
        return if (now > highest) now else highest + 1L
    }
}
