package com.pocketherbarium.notes.core.ui

import com.pocketherbarium.notes.R
import com.pocketherbarium.notes.domain.model.TraitCatalog

object TraitVisuals {

    fun shapeLabel(id: String): Int = when (id) {
        TraitCatalog.SHAPE_LOBED -> R.string.trait_lobed
        TraitCatalog.SHAPE_NEEDLE -> R.string.trait_needle
        TraitCatalog.SHAPE_FROND -> R.string.trait_frond
        TraitCatalog.SHAPE_FLESHY -> R.string.trait_fleshy
        TraitCatalog.SHAPE_BUD -> R.string.trait_bud
        else -> R.string.trait_oval
    }

    fun colourLabel(id: String): Int = when (id) {
        TraitCatalog.COLOUR_DEEP -> R.string.colour_deep
        TraitCatalog.COLOUR_PALE -> R.string.colour_pale
        TraitCatalog.COLOUR_YELLOW -> R.string.colour_yellow
        TraitCatalog.COLOUR_RUST -> R.string.colour_rust
        else -> R.string.colour_sage
    }

    fun habitatLabel(id: String): Int = when (id) {
        TraitCatalog.HABITAT_WINDOWSILL -> R.string.habitat_windowsill
        TraitCatalog.HABITAT_POND -> R.string.habitat_pond
        TraitCatalog.HABITAT_HERB_ROW -> R.string.habitat_herbrow
        else -> R.string.habitat_woodland
    }

    fun traitLabel(id: String): Int =
        if (TraitCatalog.habitats.any { it.id == id }) habitatLabel(id) else shapeLabel(id)

    fun colourSwatch(id: String): Int =
        SpriteCatalog.swatchFor(TraitCatalog.colourOrDefault(id).swatchKey)
}
