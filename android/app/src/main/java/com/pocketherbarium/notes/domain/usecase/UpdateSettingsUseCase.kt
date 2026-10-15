package com.pocketherbarium.notes.domain.usecase

import com.pocketherbarium.notes.domain.model.AppSettings
import com.pocketherbarium.notes.domain.repository.SettingsRepository

class UpdateSettingsUseCase(private val repository: SettingsRepository) {

    operator fun invoke(settings: AppSettings) {
        repository.update(settings)
    }
}
