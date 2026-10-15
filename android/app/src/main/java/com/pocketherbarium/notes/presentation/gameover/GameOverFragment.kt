package com.pocketherbarium.notes.presentation.gameover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.pocketherbarium.notes.MainActivity
import com.pocketherbarium.notes.R
import com.pocketherbarium.notes.core.config.GameConfig
import com.pocketherbarium.notes.core.di.ServiceLocator
import com.pocketherbarium.notes.core.di.ViewModelFactory
import com.pocketherbarium.notes.core.ui.SpriteCatalog
import com.pocketherbarium.notes.core.ui.TraitVisuals
import com.pocketherbarium.notes.core.util.ViewAnimations
import com.pocketherbarium.notes.databinding.FragmentGameoverBinding
import com.pocketherbarium.notes.domain.model.TraitCatalog
import kotlinx.coroutines.launch

class GameOverFragment : Fragment() {

    private var _binding: FragmentGameoverBinding? = null
    private val binding: FragmentGameoverBinding
        get() = checkNotNull(_binding)

    private val viewModel: GameOverViewModel by viewModels {
        ViewModelFactory(
            ServiceLocator.require(),
            arguments?.getLong(ARG_PLANT_ID) ?: ViewModelFactory.NO_PLANT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActions()
        playEntrance()
        observeState()
    }

    private fun setupActions() {
        binding.resultCta.setOnClickListener { target ->
            ViewAnimations.pressFeedback(target)
            val host = activity as? MainActivity ?: return@setOnClickListener
            host.navigator.restartGame()
        }
        binding.resultMenu.setOnClickListener { target ->
            ViewAnimations.pressFeedback(target)
            val host = activity as? MainActivity ?: return@setOnClickListener
            host.navigator.backToMenu()
        }
    }

    private fun playEntrance() {
        ViewAnimations.enterFromBelow(
            binding.resultPlate,
            GameConfig.PLATE_TRANSLATION_DP,
            GameConfig.PLATE_ENTER_MS,
            0L
        )
        ViewAnimations.revealRibbon(binding.resultMarker, GameConfig.SLIDE_MS, 260L)
        ViewAnimations.enterFromBelow(
            binding.resultStatsRow,
            GameConfig.ENTRY_TRANSLATION_DP,
            GameConfig.STATS_MS,
            340L
        )
        ViewAnimations.popIn(binding.resultCta, GameConfig.ENTRY_MS, 420L)
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state -> render(state) }
            }
        }
    }

    private fun render(state: GameOverUiState) {
        val safe = _binding ?: return
        val plant = state.plant
        if (plant != null) {
            safe.resultSprite.setImageResource(
                SpriteCatalog.drawableFor(TraitCatalog.spriteKeyForShape(plant.shapeId))
            )
            safe.resultName.text = plant.commonName
            safe.resultLatin.text = plant.latinName
            safe.resultLatin.isVisible = plant.latinName.isNotBlank()
            safe.resultValueShape.setText(TraitVisuals.shapeLabel(plant.shapeId))
            safe.resultValueColour.setText(TraitVisuals.colourLabel(plant.colourId))
            safe.resultValueHabitat.setText(TraitVisuals.habitatLabel(plant.habitatId))
            safe.resultCareNote.text = plant.careNote
            safe.resultMarker.isVisible = plant.annotated
        }
        safe.resultStatLibrary.bindCount(state.libraryCount)
        safe.resultStatCollection.bindCount(state.collectionCount)
        val visibleCards = listOf(safe.resultStatLibrary, safe.resultStatCollection)
            .count { it.isVisible }
        safe.resultStatsRow.isVisible = visibleCards >= GameConfig.MIN_VISIBLE_STAT_CARDS
        if (state.collectionName.isBlank()) {
            safe.resultHint.isVisible = false
        } else {
            safe.resultHint.isVisible = true
            safe.resultHint.text = getString(R.string.result_hint, state.collectionName)
        }
    }

    override fun onDestroyView() {
        _binding?.let { safe ->
            ViewAnimations.cancel(
                safe.resultPlate,
                safe.resultMarker,
                safe.resultStatsRow,
                safe.resultCta,
                safe.resultMenu
            )
            safe.resultStatLibrary.release()
            safe.resultStatCollection.release()
        }
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val ARG_PLANT_ID = "arg_plant_id"

        fun newInstance(plantId: Long): GameOverFragment {
            val fragment = GameOverFragment()
            val args = Bundle()
            args.putLong(ARG_PLANT_ID, plantId)
            fragment.arguments = args
            return fragment
        }
    }
}
