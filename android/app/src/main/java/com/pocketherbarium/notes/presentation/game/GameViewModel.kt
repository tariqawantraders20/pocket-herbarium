package com.pocketherbarium.notes.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketherbarium.notes.core.config.GameConfig
import com.pocketherbarium.notes.data.sample.SampleData
import com.pocketherbarium.notes.domain.model.Plant
import com.pocketherbarium.notes.domain.model.TraitCatalog
import com.pocketherbarium.notes.domain.usecase.GetSettingsUseCase
import com.pocketherbarium.notes.domain.usecase.SavePlantUseCase
import com.pocketherbarium.notes.domain.usecase.SetActiveCollectionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(
    private val savePlant: SavePlantUseCase,
    private val getSettings: GetSettingsUseCase,
    private val setActiveCollection: SetActiveCollectionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private var defaultLocationText = ""

    fun prepare(defaultLocation: String) {
        defaultLocationText = defaultLocation
        viewModelScope.launch {
            val settings = withContext(Dispatchers.IO) {
                try {
                    getSettings()
                } catch (e: Exception) {
                    null
                }
            }
            val habitat = settings?.defaultHabitatId ?: TraitCatalog.DEFAULT_HABITAT
            val note = SampleData.defaultCareNote(habitat)
            _state.value = GameUiState(
                habitatId = habitat,
                careNote = note,
                defaultCareNote = note,
                location = defaultLocation,
                defaultLocation = defaultLocation,
                markerOnNew = settings?.markerOnNewCards ?: true
            )
        }
    }

    fun selectShape(id: String) {
        _state.value = _state.value.copy(shapeId = TraitCatalog.shapeOrDefault(id).id)
    }

    fun selectColour(id: String) {
        _state.value = _state.value.copy(colourId = TraitCatalog.colourOrDefault(id).id)
    }

    fun selectHabitat(id: String) {
        val habitat = TraitCatalog.habitatOrDefault(id)
        val current = _state.value
        val noteFollowsDefault = current.careNote == current.defaultCareNote
        val nextDefault = SampleData.defaultCareNote(habitat.id)
        _state.value = current.copy(
            habitatId = habitat.id,
            defaultCareNote = nextDefault,
            careNote = if (noteFollowsDefault) nextDefault else current.careNote
        )
    }

    fun setName(value: String) {
        val trimmed = value.take(GameConfig.NAME_MAX_LENGTH)
        _state.value = _state.value.copy(
            name = trimmed,
            nameAtLimit = value.length >= GameConfig.NAME_MAX_LENGTH
        )
    }

    fun setCareNote(value: String) {
        _state.value = _state.value.copy(careNote = value)
    }

    fun setLocation(value: String) {
        _state.value = _state.value.copy(location = value)
    }

    fun discard() {
        val current = _state.value
        val note = SampleData.defaultCareNote(current.habitatId)
        _state.value = GameUiState(
            habitatId = current.habitatId,
            careNote = note,
            defaultCareNote = note,
            location = defaultLocationText,
            defaultLocation = defaultLocationText,
            markerOnNew = current.markerOnNew
        )
    }

    fun file(fallbackName: String, fallbackLatin: String) {
        val current = _state.value
        if (current.filedPlantId > 0L) return
        viewModelScope.launch {
            val draft = Plant(
                id = 0L,
                commonName = current.name.ifBlank { fallbackName },
                latinName = fallbackLatin,
                shapeId = current.shapeId,
                colourId = current.colourId,
                habitatId = current.habitatId,
                careNote = current.careNote.ifBlank { current.defaultCareNote },
                location = current.location.ifBlank { current.defaultLocation },
                collectionName = "",
                createdAt = 0L,
                annotated = current.annotated
            )
            val saved = withContext(Dispatchers.IO) {
                try {
                    savePlant(draft)
                } catch (e: Exception) {
                    null
                }
            }
            if (saved != null) {
                _state.value = _state.value.copy(filedPlantId = saved.id)
            }
        }
    }

    fun consumeFiled() {
        _state.value = _state.value.copy(filedPlantId = 0L)
    }

    fun activeCollection(): String = try {
        setActiveCollection.active()
    } catch (e: Exception) {
        SampleData.COLLECTION_WOODLAND
    }
}
