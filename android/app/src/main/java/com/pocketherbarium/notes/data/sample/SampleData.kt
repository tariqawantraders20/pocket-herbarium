package com.pocketherbarium.notes.data.sample

import com.pocketherbarium.notes.domain.model.Plant
import com.pocketherbarium.notes.domain.model.TraitCatalog

object SampleData {

    const val COLLECTION_WINDOWSILL = "Windowsill"
    const val COLLECTION_WOODLAND = "Woodland Walk"
    const val COLLECTION_POND = "Pond Edge"
    const val COLLECTION_HERB_ROW = "Herb Row"

    private const val DAY_MS = 86400000L

    fun collections(): List<String> = listOf(
        COLLECTION_WINDOWSILL,
        COLLECTION_WOODLAND,
        COLLECTION_POND,
        COLLECTION_HERB_ROW
    )

    fun plants(now: Long = System.currentTimeMillis()): List<Plant> = listOf(
        Plant(
            id = now - 2 * DAY_MS,
            commonName = "Lady Fern",
            latinName = "Athyrium filix-femina",
            shapeId = TraitCatalog.SHAPE_FROND,
            colourId = TraitCatalog.COLOUR_PALE,
            habitatId = TraitCatalog.HABITAT_WOODLAND,
            careNote = "Mist the fronds twice a week",
            location = "Found: shaded beech hollow",
            collectionName = COLLECTION_WOODLAND,
            createdAt = now - 2 * DAY_MS,
            annotated = true
        ),
        Plant(
            id = now - 5 * DAY_MS,
            commonName = "Garden Sage",
            latinName = "Salvia officinalis",
            shapeId = TraitCatalog.SHAPE_OVAL,
            colourId = TraitCatalog.COLOUR_SAGE,
            habitatId = TraitCatalog.HABITAT_HERB_ROW,
            careNote = "Water when the top soil is dry",
            location = "Found: kitchen herb bed",
            collectionName = COLLECTION_HERB_ROW,
            createdAt = now - 5 * DAY_MS,
            annotated = false
        ),
        Plant(
            id = now - 9 * DAY_MS,
            commonName = "Jade Plant",
            latinName = "Crassula ovata",
            shapeId = TraitCatalog.SHAPE_FLESHY,
            colourId = TraitCatalog.COLOUR_DEEP,
            habitatId = TraitCatalog.HABITAT_WINDOWSILL,
            careNote = "Soak once a month, drain fully",
            location = "Found: south window ledge",
            collectionName = COLLECTION_WINDOWSILL,
            createdAt = now - 9 * DAY_MS,
            annotated = false
        ),
        Plant(
            id = now - 14 * DAY_MS,
            commonName = "Young Oak",
            latinName = "Quercus robur",
            shapeId = TraitCatalog.SHAPE_LOBED,
            colourId = TraitCatalog.COLOUR_DEEP,
            habitatId = TraitCatalog.HABITAT_WOODLAND,
            careNote = "Keep the soil loose and shaded",
            location = "Found: north garden path",
            collectionName = COLLECTION_WOODLAND,
            createdAt = now - 14 * DAY_MS,
            annotated = false
        ),
        Plant(
            id = now - 20 * DAY_MS,
            commonName = "Blue Spruce Cutting",
            latinName = "Picea pungens",
            shapeId = TraitCatalog.SHAPE_NEEDLE,
            colourId = TraitCatalog.COLOUR_PALE,
            habitatId = TraitCatalog.HABITAT_WOODLAND,
            careNote = "Shelter from wind, mist lightly",
            location = "Found: ridge treeline",
            collectionName = COLLECTION_WOODLAND,
            createdAt = now - 20 * DAY_MS,
            annotated = true
        ),
        Plant(
            id = now - 27 * DAY_MS,
            commonName = "Marsh Marigold",
            latinName = "Caltha palustris",
            shapeId = TraitCatalog.SHAPE_BUD,
            colourId = TraitCatalog.COLOUR_YELLOW,
            habitatId = TraitCatalog.HABITAT_POND,
            careNote = "Keep the roots wet all season",
            location = "Found: reed bank by the pond",
            collectionName = COLLECTION_POND,
            createdAt = now - 27 * DAY_MS,
            annotated = false
        )
    )

    fun defaultCareNote(habitatId: String): String = when (habitatId) {
        TraitCatalog.HABITAT_WINDOWSILL -> "Bright light, water every ten days"
        TraitCatalog.HABITAT_POND -> "Keep the roots wet all season"
        TraitCatalog.HABITAT_HERB_ROW -> "Pinch the tips, water at dusk"
        else -> "Keep soil loose, water weekly"
    }

    fun defaultCollection(habitatId: String): String = when (habitatId) {
        TraitCatalog.HABITAT_WINDOWSILL -> COLLECTION_WINDOWSILL
        TraitCatalog.HABITAT_POND -> COLLECTION_POND
        TraitCatalog.HABITAT_HERB_ROW -> COLLECTION_HERB_ROW
        else -> COLLECTION_WOODLAND
    }
}
