package com.pocketherbarium.notes.core.ui

import com.pocketherbarium.notes.R

object SpriteCatalog {

    fun drawableFor(spriteKey: String): Int = when (spriteKey) {
        "leaf_oval" -> R.drawable.sprite_leaf_oval
        "leaf_lobed" -> R.drawable.sprite_leaf_lobed
        "leaf_needle" -> R.drawable.sprite_leaf_needle
        "fern_frond" -> R.drawable.sprite_fern_frond
        "succulent" -> R.drawable.sprite_succulent
        "flower_bud" -> R.drawable.sprite_flower_bud
        "press_book" -> R.drawable.sprite_press_book
        "watering_can" -> R.drawable.sprite_watering_can
        else -> R.drawable.sprite_leaf_oval
    }

    fun swatchFor(swatchKey: String): Int = when (swatchKey) {
        "swatch_deep" -> R.drawable.swatch_deep
        "swatch_pale" -> R.drawable.swatch_pale
        "swatch_yellow" -> R.drawable.swatch_yellow
        "swatch_rust" -> R.drawable.swatch_rust
        else -> R.drawable.swatch_sage
    }
}
