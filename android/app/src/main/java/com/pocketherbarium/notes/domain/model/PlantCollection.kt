package com.pocketherbarium.notes.domain.model

data class PlantCollection(
    val name: String,
    val count: Int,
    val spriteKey: String,
    val active: Boolean
)
