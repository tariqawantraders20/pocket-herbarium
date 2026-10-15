package com.pocketherbarium.notes.domain.usecase

import com.pocketherbarium.notes.domain.repository.CollectionRepository

class AddCollectionUseCase(private val repository: CollectionRepository) {

    operator fun invoke(baseName: String): String {
        val existing = repository.names()
        if (!existing.contains(baseName)) return repository.add(baseName)
        var index = 2
        while (existing.contains(baseName + " " + index)) {
            index += 1
        }
        return repository.add(baseName + " " + index)
    }
}
