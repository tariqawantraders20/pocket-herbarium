package com.pocketherbarium.notes.domain.usecase

import com.pocketherbarium.notes.domain.repository.CollectionRepository

class SetActiveCollectionUseCase(private val repository: CollectionRepository) {

    operator fun invoke(name: String) {
        if (name.isBlank()) return
        repository.setActive(name)
    }

    fun active(): String = repository.activeName()
}
