package com.pocketherbarium.notes.presentation.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.pocketherbarium.notes.R
import com.pocketherbarium.notes.core.ui.SpriteCatalog
import com.pocketherbarium.notes.databinding.ItemCollectionBinding
import com.pocketherbarium.notes.domain.model.PlantCollection

class CollectionAdapter(
    private val onSelected: (PlantCollection) -> Unit
) : RecyclerView.Adapter<CollectionAdapter.CollectionHolder>() {

    private val items = mutableListOf<PlantCollection>()

    fun submit(collections: List<PlantCollection>) {
        items.clear()
        items.addAll(collections)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionHolder {
        val binding = ItemCollectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CollectionHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectionHolder, position: Int) {
        val item = items[position]
        val binding = holder.binding
        val context = binding.root.context
        binding.collectionSprite.setImageResource(SpriteCatalog.drawableFor(item.spriteKey))
        binding.collectionName.text = item.name
        binding.collectionCount.text = item.count.toString()
        binding.collectionUnderline.visibility =
            if (item.active) View.VISIBLE else View.INVISIBLE
        binding.root.isSelected = item.active
        binding.root.contentDescription = item.name
        ViewCompat.setStateDescription(
            binding.root,
            context.getString(
                if (item.active) R.string.state_selected else R.string.state_not_selected
            )
        )
        binding.root.setOnClickListener { onSelected(item) }
    }

    override fun getItemCount(): Int = items.size

    class CollectionHolder(val binding: ItemCollectionBinding) :
        RecyclerView.ViewHolder(binding.root)
}
