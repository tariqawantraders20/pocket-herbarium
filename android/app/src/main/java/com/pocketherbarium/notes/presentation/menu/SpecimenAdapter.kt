package com.pocketherbarium.notes.presentation.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pocketherbarium.notes.core.ui.SpecimenCardView
import com.pocketherbarium.notes.databinding.ItemSpecimenBinding
import com.pocketherbarium.notes.domain.model.Plant

class SpecimenAdapter : RecyclerView.Adapter<SpecimenAdapter.SpecimenHolder>() {

    private val items = mutableListOf<Plant>()
    private var showLatinNames = true
    private var markedId = 0L

    fun submit(plants: List<Plant>, showLatin: Boolean, newestId: Long) {
        items.clear()
        items.addAll(plants)
        showLatinNames = showLatin
        markedId = newestId
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecimenHolder {
        val binding = ItemSpecimenBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SpecimenHolder(binding)
    }

    override fun onBindViewHolder(holder: SpecimenHolder, position: Int) {
        val plant = items[position]
        (holder.binding.root as SpecimenCardView).bind(
            plant,
            showLatinNames,
            plant.createdAt == markedId
        )
    }

    override fun getItemCount(): Int = items.size

    class SpecimenHolder(val binding: ItemSpecimenBinding) :
        RecyclerView.ViewHolder(binding.root)
}
