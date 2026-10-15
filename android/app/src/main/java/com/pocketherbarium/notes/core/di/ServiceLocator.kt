package com.pocketherbarium.notes.core.di

import android.content.Context
import com.pocketherbarium.notes.data.local.HerbariumPreferences
import com.pocketherbarium.notes.data.repository.CollectionRepositoryImpl
import com.pocketherbarium.notes.data.repository.PlantRepositoryImpl
import com.pocketherbarium.notes.data.repository.SettingsRepositoryImpl
import com.pocketherbarium.notes.domain.repository.CollectionRepository
import com.pocketherbarium.notes.domain.repository.PlantRepository
import com.pocketherbarium.notes.domain.repository.SettingsRepository
import com.pocketherbarium.notes.domain.usecase.AddCollectionUseCase
import com.pocketherbarium.notes.domain.usecase.GetCollectionsUseCase
import com.pocketherbarium.notes.domain.usecase.GetPlantsUseCase
import com.pocketherbarium.notes.domain.usecase.GetSettingsUseCase
import com.pocketherbarium.notes.domain.usecase.ResetLibraryUseCase
import com.pocketherbarium.notes.domain.usecase.SavePlantUseCase
import com.pocketherbarium.notes.domain.usecase.SetActiveCollectionUseCase
import com.pocketherbarium.notes.domain.usecase.UpdateSettingsUseCase

class ServiceLocator private constructor(context: Context) {

    private val preferences = HerbariumPreferences(context)

    private val plantRepository: PlantRepository = PlantRepositoryImpl(preferences)
    private val collectionRepository: CollectionRepository = CollectionRepositoryImpl(preferences)
    private val settingsRepository: SettingsRepository = SettingsRepositoryImpl(preferences)

    val getPlants = GetPlantsUseCase(plantRepository)
    val savePlant = SavePlantUseCase(plantRepository, collectionRepository)
    val getCollections = GetCollectionsUseCase(plantRepository, collectionRepository)
    val setActiveCollection = SetActiveCollectionUseCase(collectionRepository)
    val addCollection = AddCollectionUseCase(collectionRepository)
    val resetLibrary = ResetLibraryUseCase(plantRepository, collectionRepository)
    val getSettings = GetSettingsUseCase(settingsRepository)
    val updateSettings = UpdateSettingsUseCase(settingsRepository)

    companion object {

        @Volatile
        private var instance: ServiceLocator? = null

        fun init(context: Context): ServiceLocator {
            val existing = instance
            if (existing != null) return existing
            synchronized(this) {
                val created = instance ?: ServiceLocator(context.applicationContext)
                instance = created
                return created
            }
        }

        fun require(): ServiceLocator =
            instance ?: throw IllegalStateException("ServiceLocator is not initialised")
    }
}
