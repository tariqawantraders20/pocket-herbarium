package com.pocketherbarium.notes.presentation.menu

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.pocketherbarium.notes.MainActivity
import com.pocketherbarium.notes.R
import com.pocketherbarium.notes.core.config.GameConfig
import com.pocketherbarium.notes.core.di.ServiceLocator
import com.pocketherbarium.notes.core.di.ViewModelFactory
import com.pocketherbarium.notes.core.util.ViewAnimations
import com.pocketherbarium.notes.databinding.FragmentMenuBinding
import com.pocketherbarium.notes.presentation.dialog.CollectionsDialog
import com.pocketherbarium.notes.presentation.dialog.ConfirmResetDialog
import com.pocketherbarium.notes.presentation.dialog.SettingsDialog
import kotlinx.coroutines.launch

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding: FragmentMenuBinding
        get() = checkNotNull(_binding)

    private val viewModel: MenuViewModel by viewModels {
        ViewModelFactory(ServiceLocator.require())
    }

    private val adapter = SpecimenAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStrip()
        setupActions()
        setupResultListeners()
        playEntrance()
        observeState()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    private fun setupStrip() {
        binding.menuStrip.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.menuStrip.adapter = adapter
        binding.menuStrip.setHasFixedSize(true)
    }

    private fun setupActions() {
        binding.menuCta.setOnClickListener { target ->
            ViewAnimations.pressFeedback(target)
            val host = activity as? MainActivity ?: return@setOnClickListener
            host.navigator.showGame()
        }
        binding.menuCollections.setOnClickListener { target ->
            ViewAnimations.pressFeedback(target)
            CollectionsDialog().show(parentFragmentManager, CollectionsDialog.TAG)
        }
        binding.menuReset.setOnClickListener { target ->
            ViewAnimations.pressFeedback(target)
            ConfirmResetDialog().show(parentFragmentManager, ConfirmResetDialog.TAG)
        }
        binding.menuSettings.setOnClickListener {
            SettingsDialog().show(parentFragmentManager, SettingsDialog.TAG)
        }
    }

    private fun setupResultListeners() {
        parentFragmentManager.setFragmentResultListener(
            ConfirmResetDialog.REQUEST_KEY,
            viewLifecycleOwner
        ) { _, _ -> viewModel.resetLibraryContents() }
        parentFragmentManager.setFragmentResultListener(
            SettingsDialog.REQUEST_KEY,
            viewLifecycleOwner
        ) { _, _ -> viewModel.refresh() }
        parentFragmentManager.setFragmentResultListener(
            CollectionsDialog.REQUEST_KEY,
            viewLifecycleOwner
        ) { _, _ -> viewModel.refresh() }
    }

    private fun playEntrance() {
        ViewAnimations.enterFromBelow(
            binding.menuEyebrow,
            GameConfig.ENTRY_TRANSLATION_DP,
            GameConfig.ENTRY_MS,
            0L
        )
        ViewAnimations.enterFromBelow(
            binding.menuTitleTop,
            GameConfig.ENTRY_TRANSLATION_DP,
            GameConfig.ENTRY_MS,
            GameConfig.STAGGER_MS
        )
        ViewAnimations.enterFromBelow(
            binding.menuTitleBottom,
            GameConfig.ENTRY_TRANSLATION_DP,
            GameConfig.ENTRY_MS,
            GameConfig.STAGGER_MS
        )
        ViewAnimations.expandRule(binding.menuRule, GameConfig.RULE_MS, 180L)
        ViewAnimations.enterFromBelow(
            binding.menuTagline,
            GameConfig.ENTRY_TRANSLATION_DP,
            GameConfig.ENTRY_MS,
            GameConfig.STAGGER_MS * 2
        )
        ViewAnimations.enterFromBelow(
            binding.menuStatsRow,
            GameConfig.ENTRY_TRANSLATION_DP,
            GameConfig.STATS_MS,
            260L
        )
        ViewAnimations.enterFromBelow(
            binding.menuStrip,
            GameConfig.ENTRY_TRANSLATION_DP,
            GameConfig.STATS_MS,
            340L
        )
        ViewAnimations.popIn(binding.menuCta, GameConfig.ENTRY_MS, 420L)
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state -> render(state) }
            }
        }
    }

    private fun render(state: MenuUiState) {
        val safe = _binding ?: return
        val showStrip = !state.loading && state.plants.isNotEmpty()
        safe.menuSkeleton.isVisible = state.loading && state.plants.isEmpty()
        safe.menuStrip.isVisible = showStrip
        safe.menuEmpty.isVisible = state.isEmpty
        safe.menuBanner.isVisible = state.storageError
        adapter.submit(state.plants, state.showLatinNames, state.newestId)
        safe.menuStatSpecimens.bindCount(state.specimenCount)
        safe.menuStatCollections.bindCount(state.collectionCount)
        safe.menuStatLast.bindText(lastEntryLabel(state))
        val visibleCards = listOf(
            safe.menuStatSpecimens,
            safe.menuStatCollections,
            safe.menuStatLast
        ).count { it.isVisible }
        safe.menuStatsRow.isVisible = visibleCards >= GameConfig.MIN_VISIBLE_STAT_CARDS
    }

    private fun lastEntryLabel(state: MenuUiState): String {
        if (state.plants.isEmpty()) return ""
        if (state.daysSinceLastEntry < 1) return getString(R.string.today_label)
        return getString(R.string.days_label, state.daysSinceLastEntry)
    }

    override fun onDestroyView() {
        _binding?.let { safe ->
            ViewAnimations.cancel(
                safe.menuEyebrow,
                safe.menuTitleTop,
                safe.menuTitleBottom,
                safe.menuRule,
                safe.menuTagline,
                safe.menuStatsRow,
                safe.menuStrip,
                safe.menuCta
            )
            safe.menuStatSpecimens.release()
            safe.menuStatCollections.release()
            safe.menuStatLast.release()
            safe.menuStrip.adapter = null
        }
        super.onDestroyView()
        _binding = null
    }
}
