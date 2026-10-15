package com.pocketherbarium.notes.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketherbarium.notes.core.util.RelativeDate
import com.pocketherbarium.notes.domain.usecase.GetCollectionsUseCase
import com.pocketherbarium.notes.domain.usecase.GetPlantsUseCase
import com.pocketherbarium.notes.domain.usecase.GetSettingsUseCase
import com.pocketherbarium.notes.domain.usecase.ResetLibraryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MenuViewModel(
    private val getPlants: GetPlantsUseCase,
    private val getCollections: GetCollectionsUseCase,
    private val getSettings: GetSettingsUseCase,
    private val resetLibrary: ResetLibraryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            _state.value = withContext(Dispatchers.IO) { readSnapshot() }
        }
    }

    fun resetLibraryContents() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    resetLibrary()
                } catch (e: Exception) {
                    emptyList()
                }
            }
            _state.value = withContext(Dispatchers.IO) { readSnapshot() }
        }
    }

    private fun readSnapshot(): MenuUiState {
        return try {
            val plants = getPlants()
            val collections = getCollections()
            val settings = getSettings()
            val newest = plants.maxOfOrNull { it.createdAt } ?: 0L
            MenuUiState(
                loading = false,
                plants = plants,
                collections = collections,
                showLatinNames = settings.showLatinNames,
                storageError = getPlants.readFailed(),
                daysSinceLastEntry = RelativeDate.daysBetween(newest, System.currentTimeMillis())
            )
        } catch (e: Exception) {
            MenuUiState(loading = false, storageError = true)
        }
    }
}
