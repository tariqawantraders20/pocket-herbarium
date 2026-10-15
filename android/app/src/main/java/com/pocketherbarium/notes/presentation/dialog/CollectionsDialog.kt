package com.pocketherbarium.notes.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pocketherbarium.notes.R
import com.pocketherbarium.notes.core.di.ServiceLocator
import com.pocketherbarium.notes.core.util.ViewAnimations
import com.pocketherbarium.notes.databinding.DialogCollectionsBinding
import com.pocketherbarium.notes.domain.model.PlantCollection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CollectionsDialog : DialogFragment() {

    private var _binding: DialogCollectionsBinding? = null
    private val binding: DialogCollectionsBinding
        get() = checkNotNull(_binding)

    private val adapter = CollectionAdapter { collection -> choose(collection) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_PocketHerbarium_Dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogCollectionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.collectionsList.layoutManager = LinearLayoutManager(requireContext())
        binding.collectionsList.adapter = adapter
        binding.collectionsClose.setOnClickListener { dismissAllowingStateLoss() }
        binding.collectionsNew.setOnClickListener { target ->
            ViewAnimations.pressFeedback(target)
            createCollection()
        }
        load()
    }

    private fun load() {
        viewLifecycleOwner.lifecycleScope.launch {
            val data = withContext(Dispatchers.IO) { readCollections() }
            if (_binding == null) return@launch
            adapter.submit(data)
        }
    }

    private fun choose(collection: PlantCollection) {
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    ServiceLocator.require().setActiveCollection(collection.name)
                } catch (e: Exception) {
                    Unit
                }
            }
            publish()
            load()
        }
    }

    private fun createCollection() {
        val proposed = getString(R.string.new_collection_name)
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    ServiceLocator.require().addCollection(proposed)
                } catch (e: Exception) {
                    ""
                }
            }
            publish()
            load()
        }
    }

    private fun publish() {
        if (!isAdded) return
        parentFragmentManager.setFragmentResult(REQUEST_KEY, Bundle())
    }

    private fun readCollections(): List<PlantCollection> = try {
        ServiceLocator.require().getCollections()
    } catch (e: Exception) {
        emptyList()
    }

    override fun onDestroyView() {
        _binding?.let { safe ->
            ViewAnimations.cancel(safe.collectionsNew, safe.collectionsClose)
            safe.collectionsList.adapter = null
        }
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "collections_dialog"
        const val REQUEST_KEY = "request_collection_changed"
    }
}
