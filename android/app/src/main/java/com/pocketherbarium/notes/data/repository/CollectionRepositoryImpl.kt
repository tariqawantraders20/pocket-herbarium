package com.pocketherbarium.notes.data.repository

import com.pocketherbarium.notes.data.local.HerbariumPreferences
import com.pocketherbarium.notes.data.local.PlantSerializer
import com.pocketherbarium.notes.data.sample.SampleData
import com.pocketherbarium.notes.domain.repository.CollectionRepository

class CollectionRepositoryImpl(private val preferences: HerbariumPreferences) : CollectionRepository {

    override fun names(): List<String> {
        val stored = PlantSerializer.decodeNames(preferences.collections())
        if (stored.isNotEmpty()) return stored
        val fresh = SampleData.collections()
        preferences.writeCollections(PlantSerializer.encodeNames(fresh))
        return fresh
    }

    override fun activeName(): String {
        val stored = preferences.activeCollection()
        val available = names()
        if (stored.isNotEmpty() && available.contains(stored)) return stored
        val fallback = available.firstOrNull() ?: SampleData.COLLECTION_WOODLAND
        preferences.writeActiveCollection(fallback)
        return fallback
    }

    override fun setActive(name: String) {
        if (!names().contains(name)) return
        preferences.writeActiveCollection(name)
    }

    override fun add(name: String): String {
        val current = names().toMutableList()
        if (!current.contains(name)) {
            current.add(name)
            preferences.writeCollections(PlantSerializer.encodeNames(current))
        }
        preferences.writeActiveCollection(name)
        return name
    }

    override fun reset() {
        preferences.writeCollections(PlantSerializer.encodeNames(SampleData.collections()))
        preferences.writeActiveCollection(SampleData.COLLECTION_WOODLAND)
    }
}
