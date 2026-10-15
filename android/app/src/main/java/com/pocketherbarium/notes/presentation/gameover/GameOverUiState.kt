package com.pocketherbarium.notes.presentation.gameover

import com.pocketherbarium.notes.domain.model.Plant

data class GameOverUiState(
    val loading: Boolean = true,
    val plant: Plant? = null,
    val libraryCount: Int = 0,
    val collectionCount: Int = 0,
    val collectionName: String = ""
)
