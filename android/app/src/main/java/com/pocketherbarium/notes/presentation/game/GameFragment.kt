package com.pocketherbarium.notes.presentation.game

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.button.MaterialButton
import com.pocketherbarium.notes.MainActivity
import com.pocketherbarium.notes.R
import com.pocketherbarium.notes.core.config.GameConfig
import com.pocketherbarium.notes.core.di.ServiceLocator
import com.pocketherbarium.notes.core.di.ViewModelFactory
import com.pocketherbarium.notes.core.ui.SpriteCatalog
import com.pocketherbarium.notes.core.ui.TraitVisuals
import com.pocketherbarium.notes.core.util.ViewAnimations
import com.pocketherbarium.notes.databinding.FragmentGameBinding
import com.pocketherbarium.notes.domain.model.TraitCatalog
import com.pocketherbarium.notes.presentation.dialog.CollectionsDialog
import kotlinx.coroutines.launch

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = checkNotNull(_binding)

    private val viewModel: GameViewModel by viewModels {
        ViewModelFactory(ServiceLocator.require())
    }

    private var syncing = false
    private var prefilled = false
    private var renderedShapeId = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.gameRoot.requestFocus()
        setupTraits()
        setupSwatches()
        setupHabitats()
        setupInputs()
        setupActions()
        observeState()
        if (viewModel.state.value.defaultCareNote.isBlank()) {
            viewModel.prepare(getString(R.string.default_location))
        }
    }

    private fun setupTraits() {
        binding.gameTraitRow.setTraits(TraitCatalog.leafShapes, TRAIT_COLUMNS)
        binding.gameTraitRow.onTraitSelected = { trait -> viewModel.selectShape(trait.id) }
    }

    private fun setupSwatches() {
        swatchViews().forEach { entry ->
            entry.value.setOnClickListener { target ->
                ViewAnimations.traitPop(target)
                viewModel.selectColour(entry.key)
            }
        }
    }

    private fun setupHabitats() {
        habitatButtons().forEach { entry ->
            entry.value.setOnClickListener { target ->
                ViewAnimations.traitPop(target)
                viewModel.selectHabitat(entry.key)
            }
        }
    }

    private fun setupInputs() {
        binding.gameNameInput.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(GameConfig.NAME_MAX_LENGTH))
        binding.gameNameInput.doAfterTextChanged { editable ->
            if (!syncing) {
                viewModel.setName(editable?.toString().orEmpty())
            }
        }
        binding.gameCareInput.doAfterTextChanged { editable ->
            if (!syncing) {
                viewModel.setCareNote(editable?.toString().orEmpty())
            }
        }
        binding.gameLocationInput.doAfterTextChanged { editable ->
            if (!syncing) {
                viewModel.setLocation(editable?.toString().orEmpty())
            }
        }
    }

    private fun setupActions() {
        binding.gameCta.setOnClickListener { target ->
            ViewAnimations.pressFeedback(target)
            viewModel.file(
                getString(R.string.default_specimen_name),
                getString(R.string.default_latin_name)
            )
        }
        binding.gameDiscard.setOnClickListener { target ->
            ViewAnimations.pressFeedback(target)
            prefilled = false
            renderedShapeId = ""
            viewModel.discard()
            binding.gameTraitRow.setSelectedTrait(TraitCatalog.DEFAULT_SHAPE)
        }
        binding.gameCollections.setOnClickListener { target ->
            ViewAnimations.pressFeedback(target)
            CollectionsDialog().show(parentFragmentManager, CollectionsDialog.TAG)
        }
        binding.gameBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state -> render(state) }
            }
        }
    }

    private fun render(state: GameUiState) {
        val safe = _binding ?: return
        prefill(state)
        renderPreview(state)
        renderFacts(state)
        renderSelection(state)
        safe.gameNameField.helperText =
            if (state.nameAtLimit) getString(R.string.game_name_helper) else null
        safe.gameDiscard.isEnabled = !state.pristine
        safe.gameDiscard.alpha = if (state.pristine) DISABLED_ALPHA else 1f
        ViewCompat.setStateDescription(
            safe.gameDiscard,
            if (state.pristine) getString(R.string.state_nothing_to_discard) else ""
        )
        if (state.filedPlantId > 0L) {
            openResult(state.filedPlantId)
        }
    }

    private fun prefill(state: GameUiState) {
        val safe = _binding ?: return
        if (prefilled || state.defaultCareNote.isBlank()) return
        prefilled = true
        syncing = true
        safe.gameNameInput.setText(state.name)
        safe.gameCareInput.setText(state.careNote)
        safe.gameLocationInput.setText(state.location)
        syncing = false
    }

    private fun renderPreview(state: GameUiState) {
        val safe = _binding ?: return
        if (renderedShapeId != state.shapeId) {
            renderedShapeId = state.shapeId
            safe.gamePreviewSprite.setImageResource(
                SpriteCatalog.drawableFor(TraitCatalog.spriteKeyForShape(state.shapeId))
            )
            ViewAnimations.crossFade(safe.gamePreviewSprite, GameConfig.CROSSFADE_MS)
        }
        safe.gamePreviewName.text =
            state.name.ifBlank { getString(R.string.default_specimen_name) }
        safe.gamePreviewHabitat.setText(TraitVisuals.habitatLabel(state.habitatId))
    }

    private fun renderFacts(state: GameUiState) {
        val safe = _binding ?: return
        safe.gameFactShape.setText(TraitVisuals.shapeLabel(state.shapeId))
        safe.gameFactColour.setText(TraitVisuals.colourLabel(state.colourId))
        safe.gameFactSpot.setText(TraitVisuals.habitatLabel(state.habitatId))
        ViewAnimations.crossFade(safe.gameFactsStrip, GameConfig.CROSSFADE_MS)
    }

    private fun renderSelection(state: GameUiState) {
        binding.gameTraitRow.setSelectedTrait(state.shapeId)
        val selectedText = getString(R.string.state_selected)
        val idleText = getString(R.string.state_not_selected)
        swatchViews().forEach { entry ->
            val chosen = entry.key == state.colourId
            entry.value.isSelected = chosen
            ViewCompat.setStateDescription(entry.value, if (chosen) selectedText else idleText)
        }
        habitatButtons().forEach { entry ->
            val chosen = entry.key == state.habitatId
            entry.value.isSelected = chosen
            ViewCompat.setStateDescription(entry.value, if (chosen) selectedText else idleText)
        }
    }

    private fun openResult(plantId: Long) {
        if (!isAdded) return
        val host = activity as? MainActivity ?: return
        if (!host.navigator.showResult(plantId)) return
        viewModel.consumeFiled()
    }

    private fun swatchViews(): Map<String, FrameLayout> = mapOf(
        TraitCatalog.COLOUR_SAGE to binding.gameSwatchSage,
        TraitCatalog.COLOUR_DEEP to binding.gameSwatchDeep,
        TraitCatalog.COLOUR_PALE to binding.gameSwatchPale,
        TraitCatalog.COLOUR_YELLOW to binding.gameSwatchYellow,
        TraitCatalog.COLOUR_RUST to binding.gameSwatchRust
    )

    private fun habitatButtons(): Map<String, MaterialButton> = mapOf(
        TraitCatalog.HABITAT_WINDOWSILL to binding.gameHabitatWindowsill,
        TraitCatalog.HABITAT_WOODLAND to binding.gameHabitatWoodland,
        TraitCatalog.HABITAT_POND to binding.gameHabitatPond,
        TraitCatalog.HABITAT_HERB_ROW to binding.gameHabitatHerbrow
    )

    override fun onDestroyView() {
        _binding?.let { safe ->
            ViewAnimations.cancel(
                safe.gameCta,
                safe.gameDiscard,
                safe.gameCollections,
                safe.gamePreviewSprite,
                safe.gameFactsStrip
            )
            safe.gameTraitRow.release()
        }
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TRAIT_COLUMNS = 3
        private const val DISABLED_ALPHA = 0.45f
    }
}
