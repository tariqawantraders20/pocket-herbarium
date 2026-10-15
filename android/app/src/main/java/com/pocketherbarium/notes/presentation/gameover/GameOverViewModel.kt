package com.pocketherbarium.notes.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketherbarium.notes.domain.usecase.GetCollectionsUseCase
import com.pocketherbarium.notes.domain.usecase.GetPlantsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameOverViewModel(
    private val getPlants: GetPlantsUseCase,
    private val getCollections: GetCollectionsUseCase,
    private val plantId: Long
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _state.value = withContext(Dispatchers.IO) { readSnapshot() }
        }
    }

    private fun readSnapshot(): GameOverUiState {
        return try {
            val plants = getPlants()
            val plant = plants.firstOrNull { it.id == plantId } ?: plants.firstOrNull()
            val collectionName = plant?.collectionName.orEmpty()
            val collections = getCollections()
            val count = collections.firstOrNull { it.name == collectionName }?.count ?: 0
            GameOverUiState(
                loading = false,
                plant = plant,
                libraryCount = plants.size,
                collectionCount = count,
                collectionName = collectionName
            )
        } catch (e: Exception) {
            GameOverUiState(loading = false)
        }
    }
}
