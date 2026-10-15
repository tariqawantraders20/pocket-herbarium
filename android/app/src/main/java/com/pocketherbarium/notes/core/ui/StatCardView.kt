package com.pocketherbarium.notes.core.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.pocketherbarium.notes.R
import com.pocketherbarium.notes.core.config.GameConfig
import com.pocketherbarium.notes.core.util.ViewAnimations
import com.pocketherbarium.notes.databinding.ViewStatCardBinding

class StatCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewStatCardBinding

    init {
        orientation = VERTICAL
        background = ContextCompat.getDrawable(context, R.drawable.surface_paper_card)
        val inset = resources.getDimensionPixelSize(R.dimen.block_gap)
        setPadding(inset, inset, inset, inset)
        binding = ViewStatCardBinding.inflate(LayoutInflater.from(context), this)
        var accent = ContextCompat.getColor(context, R.color.herb_green_primary)
        if (attrs != null) {
            val typed = context.obtainStyledAttributes(attrs, R.styleable.StatCardView)
            binding.statLabel.text = typed.getString(R.styleable.StatCardView_statLabel).orEmpty()
            binding.statValue.text = typed.getString(R.styleable.StatCardView_statValue).orEmpty()
            accent = typed.getColor(R.styleable.StatCardView_statAccent, accent)
            typed.recycle()
        }
        applyAccent(accent)
    }

    fun setAccent(color: Int) {
        applyAccent(color)
    }

    fun setLabel(text: CharSequence) {
        binding.statLabel.text = text
    }

    fun setValue(text: CharSequence) {
        binding.statValue.text = text
    }

    fun bindCount(count: Int) {
        isVisible = count > 0
        binding.statValue.text = count.toString()
    }

    fun bindText(text: String) {
        isVisible = text.isNotBlank()
        binding.statValue.text = text
    }

    fun animateValue() {
        ViewAnimations.refreshValue(binding.statValue, GameConfig.COUNT_MS)
    }

    fun release() {
        ViewAnimations.cancel(binding.statValue)
    }

    private fun applyAccent(color: Int) {
        val dot = binding.statDot.background?.mutate()
        if (dot != null) {
            dot.setTint(color)
            binding.statDot.background = dot
        }
        binding.statValue.setTextColor(color)
    }
}
