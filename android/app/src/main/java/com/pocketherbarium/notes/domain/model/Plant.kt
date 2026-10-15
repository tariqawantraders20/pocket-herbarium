package com.pocketherbarium.notes.domain.model

data class Plant(
    val id: Long,
    val commonName: String,
    val latinName: String,
    val shapeId: String,
    val colourId: String,
    val habitatId: String,
    val careNote: String,
    val location: String,
    val collectionName: String,
    val createdAt: Long,
    val annotated: Boolean
)
