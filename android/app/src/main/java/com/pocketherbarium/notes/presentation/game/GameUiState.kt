package com.pocketherbarium.notes.presentation.game

import com.pocketherbarium.notes.domain.model.TraitCatalog

data class GameUiState(
    val name: String = "",
    val shapeId: String = TraitCatalog.DEFAULT_SHAPE,
    val colourId: String = TraitCatalog.DEFAULT_COLOUR,
    val habitatId: String = TraitCatalog.DEFAULT_HABITAT,
    val careNote: String = "",
    val defaultCareNote: String = "",
    val location: String = "",
    val defaultLocation: String = "",
    val markerOnNew: Boolean = true,
    val nameAtLimit: Boolean = false,
    val filedPlantId: Long = 0L
) {

    val pristine: Boolean
        get() = name.isBlank() &&
            shapeId == TraitCatalog.DEFAULT_SHAPE &&
            colourId == TraitCatalog.DEFAULT_COLOUR &&
            careNote == defaultCareNote &&
            location == defaultLocation

    val annotated: Boolean
        get() = markerOnNew && careNote.isNotBlank() && careNote != defaultCareNote
}
