package com.pocketherbarium.notes.domain.model

data class AppSettings(
    val showLatinNames: Boolean,
    val defaultHabitatId: String,
    val markerOnNewCards: Boolean
)
