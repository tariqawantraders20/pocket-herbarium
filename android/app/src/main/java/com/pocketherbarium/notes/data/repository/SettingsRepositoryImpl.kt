package com.pocketherbarium.notes.data.repository

import com.pocketherbarium.notes.data.local.HerbariumPreferences
import com.pocketherbarium.notes.domain.model.AppSettings
import com.pocketherbarium.notes.domain.model.TraitCatalog
import com.pocketherbarium.notes.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val preferences: HerbariumPreferences) : SettingsRepository {

    override fun settings(): AppSettings {
        val habitat = preferences.defaultHabitat()
        return AppSettings(
            showLatinNames = preferences.showLatin(),
            defaultHabitatId = if (habitat.isBlank()) TraitCatalog.DEFAULT_HABITAT else habitat,
            markerOnNewCards = preferences.markerOnNew()
        )
    }

    override fun update(settings: AppSettings) {
        preferences.writeShowLatin(settings.showLatinNames)
        preferences.writeDefaultHabitat(TraitCatalog.habitatOrDefault(settings.defaultHabitatId).id)
        preferences.writeMarkerOnNew(settings.markerOnNewCards)
    }
}
