package com.pocketherbarium.notes.data.local

import com.pocketherbarium.notes.domain.model.Plant

object PlantSerializer {

    private const val FIELD = "|"
    private const val RECORD = "\n"
    private const val FIELD_COUNT = 11

    fun encodeAll(plants: List<Plant>): String =
        plants.joinToString(RECORD) { encode(it) }

    fun decodeAll(raw: String): List<Plant> {
        if (raw.isBlank()) return emptyList()
        return raw.split(RECORD).mapNotNull { decode(it) }
    }

    fun encodeNames(names: List<String>): String =
        names.joinToString(FIELD) { clean(it) }

    fun decodeNames(raw: String): List<String> {
        if (raw.isBlank()) return emptyList()
        return raw.split(FIELD).map { it.trim() }.filter { it.isNotEmpty() }
    }

    private fun encode(plant: Plant): String = listOf(
        plant.id.toString(),
        clean(plant.commonName),
        clean(plant.latinName),
        clean(plant.shapeId),
        clean(plant.colourId),
        clean(plant.habitatId),
        clean(plant.careNote),
        clean(plant.location),
        clean(plant.collectionName),
        plant.createdAt.toString(),
        if (plant.annotated) "1" else "0"
    ).joinToString(FIELD)

    private fun decode(record: String): Plant? {
        val parts = record.split(FIELD)
        if (parts.size < FIELD_COUNT) return null
        val id = parts[0].toLongOrNull() ?: return null
        val createdAt = parts[9].toLongOrNull() ?: id
        return Plant(
            id = id,
            commonName = parts[1],
            latinName = parts[2],
            shapeId = parts[3],
            colourId = parts[4],
            habitatId = parts[5],
            careNote = parts[6],
            location = parts[7],
            collectionName = parts[8],
            createdAt = createdAt,
            annotated = parts[10] == "1"
        )
    }

    private fun clean(value: String): String =
        value.replace(FIELD, " ").replace(RECORD, " ").replace("\r", " ").trim()
}
