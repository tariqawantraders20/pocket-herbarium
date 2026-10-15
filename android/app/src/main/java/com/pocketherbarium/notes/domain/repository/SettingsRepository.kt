package com.pocketherbarium.notes.domain.repository

import com.pocketherbarium.notes.domain.model.AppSettings

interface SettingsRepository {

    fun settings(): AppSettings

    fun update(settings: AppSettings)
}
