package com.pocketherbarium.notes.presentation.menu

import com.pocketherbarium.notes.domain.model.Plant
import com.pocketherbarium.notes.domain.model.PlantCollection

data class MenuUiState(
    val loading: Boolean = true,
    val plants: List<Plant> = emptyList(),
    val collections: List<PlantCollection> = emptyList(),
    val showLatinNames: Boolean = true,
    val storageError: Boolean = false,
    val daysSinceLastEntry: Int = 0
) {

    val specimenCount: Int
        get() = plants.size

    val collectionCount: Int
        get() = collections.count { it.count > 0 }

    val isEmpty: Boolean
        get() = !loading && plants.isEmpty()

    val newestId: Long
        get() = plants.maxOfOrNull { it.createdAt } ?: 0L
}
