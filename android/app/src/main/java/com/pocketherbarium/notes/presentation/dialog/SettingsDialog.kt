package com.pocketherbarium.notes.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.pocketherbarium.notes.R
import com.pocketherbarium.notes.core.di.ServiceLocator
import com.pocketherbarium.notes.core.util.ViewAnimations
import com.pocketherbarium.notes.databinding.DialogSettingsBinding
import com.pocketherbarium.notes.domain.model.AppSettings
import com.pocketherbarium.notes.domain.model.TraitCatalog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsDialog : DialogFragment() {

    private var _binding: DialogSettingsBinding? = null
    private val binding: DialogSettingsBinding
        get() = checkNotNull(_binding)

    private var habitatId = TraitCatalog.DEFAULT_HABITAT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_PocketHerbarium_Dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingsHabitatRow.setTraits(TraitCatalog.habitatTraits, HABITAT_COLUMNS)
        binding.settingsHabitatRow.onTraitSelected = { trait ->
            habitatId = trait.id
        }
        binding.settingsSwitchLatin.setOnCheckedChangeListener { button, checked ->
            ViewCompat.setStateDescription(button, stateText(checked))
        }
        binding.settingsSwitchMarker.setOnCheckedChangeListener { button, checked ->
            ViewCompat.setStateDescription(button, stateText(checked))
        }
        binding.settingsDone.setOnClickListener { target ->
            ViewAnimations.pressFeedback(target)
            persist()
        }
        load()
    }

    private fun load() {
        viewLifecycleOwner.lifecycleScope.launch {
            val settings = withContext(Dispatchers.IO) { readSettings() }
            val safe = _binding ?: return@launch
            habitatId = settings.defaultHabitatId
            safe.settingsSwitchLatin.isChecked = settings.showLatinNames
            safe.settingsSwitchMarker.isChecked = settings.markerOnNewCards
            safe.settingsHabitatRow.setSelectedTrait(settings.defaultHabitatId)
            ViewCompat.setStateDescription(
                safe.settingsSwitchLatin,
                stateText(settings.showLatinNames)
            )
            ViewCompat.setStateDescription(
                safe.settingsSwitchMarker,
                stateText(settings.markerOnNewCards)
            )
        }
    }

    private fun persist() {
        val safe = _binding ?: return
        val settings = AppSettings(
            showLatinNames = safe.settingsSwitchLatin.isChecked,
            defaultHabitatId = habitatId,
            markerOnNewCards = safe.settingsSwitchMarker.isChecked
        )
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    ServiceLocator.require().updateSettings(settings)
                } catch (e: Exception) {
                    Unit
                }
            }
            if (isAdded) {
                parentFragmentManager.setFragmentResult(REQUEST_KEY, Bundle())
            }
            dismissAllowingStateLoss()
        }
    }

    private fun readSettings(): AppSettings = try {
        ServiceLocator.require().getSettings()
    } catch (e: Exception) {
        AppSettings(true, TraitCatalog.DEFAULT_HABITAT, true)
    }

    private fun stateText(checked: Boolean): String =
        getString(if (checked) R.string.state_on else R.string.state_off)

    override fun onDestroyView() {
        _binding?.let { safe ->
            ViewAnimations.cancel(safe.settingsDone)
            safe.settingsHabitatRow.release()
        }
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "settings_dialog"
        const val REQUEST_KEY = "request_settings_changed"
        private const val HABITAT_COLUMNS = 2
    }
}
