package com.pocketherbarium.notes.core.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.card.MaterialCardView
import com.pocketherbarium.notes.R
import com.pocketherbarium.notes.databinding.ViewSpecimenCardBinding
import com.pocketherbarium.notes.domain.model.Plant
import com.pocketherbarium.notes.domain.model.TraitCatalog

class SpecimenCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: ViewSpecimenCardBinding

    init {
        radius = resources.getDimension(R.dimen.card_radius)
        cardElevation = resources.getDimension(R.dimen.hairline)
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.herb_paper_card))
        strokeWidth = resources.getDimensionPixelSize(R.dimen.hairline)
        strokeColor = ContextCompat.getColor(context, R.color.herb_hairline)
        binding = ViewSpecimenCardBinding.inflate(LayoutInflater.from(context), this)
    }

    fun bind(plant: Plant, showLatin: Boolean, showMarker: Boolean) {
        binding.specimenThumb.setImageResource(
            SpriteCatalog.drawableFor(TraitCatalog.spriteKeyForShape(plant.shapeId))
        )
        binding.specimenName.text = plant.commonName
        binding.specimenLatin.text = plant.latinName
        binding.specimenLatin.isVisible = showLatin && plant.latinName.isNotBlank()
        binding.specimenMarker.isVisible = showMarker
        contentDescription = plant.commonName
    }
}
