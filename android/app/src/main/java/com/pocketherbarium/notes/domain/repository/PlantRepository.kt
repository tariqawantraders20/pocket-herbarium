package com.pocketherbarium.notes.domain.repository

import com.pocketherbarium.notes.domain.model.Plant

interface PlantRepository {

    fun plants(): List<Plant>

    fun find(id: Long): Plant?

    fun save(plant: Plant): Plant

    fun reset(): List<Plant>

    fun lastReadFailed(): Boolean
}
