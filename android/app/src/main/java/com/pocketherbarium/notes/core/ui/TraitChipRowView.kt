package com.pocketherbarium.notes.core.ui

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import com.pocketherbarium.notes.R
import com.pocketherbarium.notes.core.util.ViewAnimations
import com.pocketherbarium.notes.databinding.ViewTraitRowBinding
import com.pocketherbarium.notes.domain.model.PlantTrait

class TraitChipRowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewTraitRowBinding
    private val tiles = LinkedHashMap<String, View>()
    private val markers = LinkedHashMap<String, View>()
    private var selectedId = ""

    var onTraitSelected: ((PlantTrait) -> Unit)? = null

    init {
        binding = ViewTraitRowBinding.inflate(LayoutInflater.from(context), this)
    }

    fun setTraits(traits: List<PlantTrait>, columns: Int) {
        binding.traitRows.removeAllViews()
        tiles.clear()
        markers.clear()
        val gap = resources.getDimensionPixelSize(R.dimen.block_gap)
        val tileHeight = resources.getDimensionPixelSize(R.dimen.trait_tile)
        traits.chunked(columns).forEachIndexed { rowIndex, chunk ->
            val row = LinearLayout(context)
            row.orientation = LinearLayout.HORIZONTAL
            row.isBaselineAligned = false
            val rowParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            if (rowIndex > 0) {
                rowParams.topMargin = gap
            }
            row.layoutParams = rowParams
            chunk.forEachIndexed { index, trait ->
                row.addView(createTile(trait, tileHeight, if (index > 0) gap else 0))
            }
            var filler = chunk.size
            while (filler < columns) {
                row.addView(createSpacer(tileHeight, gap))
                filler += 1
            }
            binding.traitRows.addView(row)
        }
        applySelection()
    }

    fun setSelectedTrait(id: String) {
        if (selectedId == id) return
        selectedId = id
        applySelection()
    }

    fun release() {
        tiles.values.forEach { ViewAnimations.cancel(it) }
        onTraitSelected = null
    }

    private fun createTile(trait: PlantTrait, tileHeight: Int, startMargin: Int): View {
        val tile = FrameLayout(context)
        val params = LinearLayout.LayoutParams(0, tileHeight, 1f)
        params.marginStart = startMargin
        tile.layoutParams = params
        tile.background = ContextCompat.getDrawable(context, R.drawable.ripple_primary)
        tile.isClickable = true
        tile.isFocusable = true
        tile.minimumHeight = resources.getDimensionPixelSize(R.dimen.touch_min)
        tile.contentDescription = context.getString(TraitVisuals.traitLabel(trait.id))

        val column = LinearLayout(context)
        column.orientation = LinearLayout.VERTICAL
        column.gravity = Gravity.CENTER
        column.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        val sprite = ImageView(context)
        val spriteSize = resources.getDimensionPixelSize(R.dimen.trait_sprite)
        sprite.layoutParams = LinearLayout.LayoutParams(spriteSize, spriteSize)
        sprite.scaleType = ImageView.ScaleType.FIT_CENTER
        sprite.setImageResource(SpriteCatalog.drawableFor(trait.spriteKey))
        sprite.importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
        column.addView(sprite)

        val caption = TextView(context)
        caption.setTextAppearance(R.style.TextAppearance_App_Label)
        caption.setText(TraitVisuals.traitLabel(trait.id))
        caption.setTextColor(ContextCompat.getColor(context, R.color.herb_ink_soft))
        caption.gravity = Gravity.CENTER
        val captionParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        captionParams.topMargin = resources.getDimensionPixelSize(R.dimen.hairline) * 6
        caption.layoutParams = captionParams
        caption.importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
        column.addView(caption)

        tile.addView(column)

        val marker = View(context)
        val markerSize = resources.getDimensionPixelSize(R.dimen.hairline) * 6
        val markerParams = LayoutParams(markerSize, markerSize, Gravity.END or Gravity.TOP)
        markerParams.topMargin = markerSize
        markerParams.marginEnd = markerSize
        marker.layoutParams = markerParams
        marker.background = ContextCompat.getDrawable(context, R.drawable.dot_marker)
        marker.importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
        marker.isVisible = false
        tile.addView(marker)

        tile.setOnClickListener {
            ViewAnimations.traitPop(tile)
            setSelectedTrait(trait.id)
            onTraitSelected?.invoke(trait)
        }

        tiles[trait.id] = tile
        markers[trait.id] = marker
        return tile
    }

    private fun createSpacer(tileHeight: Int, startMargin: Int): View {
        val spacer = View(context)
        val params = LinearLayout.LayoutParams(0, tileHeight, 1f)
        params.marginStart = startMargin
        spacer.layoutParams = params
        spacer.importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
        return spacer
    }

    private fun applySelection() {
        val selectedText = context.getString(R.string.state_selected)
        val idleText = context.getString(R.string.state_not_selected)
        tiles.forEach { entry ->
            val chosen = entry.key == selectedId
            entry.value.isSelected = chosen
            markers[entry.key]?.isVisible = chosen
            ViewCompat.setStateDescription(entry.value, if (chosen) selectedText else idleText)
        }
    }
}
