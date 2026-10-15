package com.pocketherbarium.notes.domain.usecase

import com.pocketherbarium.notes.domain.model.AppSettings
import com.pocketherbarium.notes.domain.repository.SettingsRepository

class GetSettingsUseCase(private val repository: SettingsRepository) {

    operator fun invoke(): AppSettings = repository.settings()
}
