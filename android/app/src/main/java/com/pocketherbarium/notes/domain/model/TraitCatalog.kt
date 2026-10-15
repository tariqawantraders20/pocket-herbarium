package com.pocketherbarium.notes.domain.model

object TraitCatalog {

    const val SHAPE_OVAL = "oval"
    const val SHAPE_LOBED = "lobed"
    const val SHAPE_NEEDLE = "needle"
    const val SHAPE_FROND = "frond"
    const val SHAPE_FLESHY = "fleshy"
    const val SHAPE_BUD = "bud"

    const val COLOUR_SAGE = "sage"
    const val COLOUR_DEEP = "deep"
    const val COLOUR_PALE = "pale"
    const val COLOUR_YELLOW = "yellow"
    const val COLOUR_RUST = "rust"

    const val HABITAT_WINDOWSILL = "windowsill"
    const val HABITAT_WOODLAND = "woodland"
    const val HABITAT_POND = "pond"
    const val HABITAT_HERB_ROW = "herbrow"

    const val DEFAULT_SHAPE = SHAPE_OVAL
    const val DEFAULT_COLOUR = COLOUR_SAGE
    const val DEFAULT_HABITAT = HABITAT_WOODLAND

    val leafShapes: List<PlantTrait> = listOf(
        PlantTrait(SHAPE_OVAL, "leaf_oval"),
        PlantTrait(SHAPE_LOBED, "leaf_lobed"),
        PlantTrait(SHAPE_NEEDLE, "leaf_needle"),
        PlantTrait(SHAPE_FROND, "fern_frond"),
        PlantTrait(SHAPE_FLESHY, "succulent"),
        PlantTrait(SHAPE_BUD, "flower_bud")
    )

    val leafColours: List<LeafColour> = listOf(
        LeafColour(COLOUR_SAGE, "swatch_sage"),
        LeafColour(COLOUR_DEEP, "swatch_deep"),
        LeafColour(COLOUR_PALE, "swatch_pale"),
        LeafColour(COLOUR_YELLOW, "swatch_yellow"),
        LeafColour(COLOUR_RUST, "swatch_rust")
    )

    val habitats: List<Habitat> = listOf(
        Habitat(HABITAT_WINDOWSILL, "succulent"),
        Habitat(HABITAT_WOODLAND, "fern_frond"),
        Habitat(HABITAT_POND, "flower_bud"),
        Habitat(HABITAT_HERB_ROW, "leaf_oval")
    )

    val habitatTraits: List<PlantTrait> = habitats.map { PlantTrait(it.id, it.spriteKey) }

    fun shapeOrDefault(id: String): PlantTrait =
        leafShapes.firstOrNull { it.id == id } ?: leafShapes.first()

    fun colourOrDefault(id: String): LeafColour =
        leafColours.firstOrNull { it.id == id } ?: leafColours.first()

    fun habitatOrDefault(id: String): Habitat =
        habitats.firstOrNull { it.id == id } ?: habitats[1]

    fun spriteKeyForShape(id: String): String = shapeOrDefault(id).spriteKey

    fun spriteKeyForHabitat(id: String): String = habitatOrDefault(id).spriteKey
}
