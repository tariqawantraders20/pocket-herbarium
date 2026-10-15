package com.pocketherbarium.notes.core.util

import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import com.pocketherbarium.notes.core.config.GameConfig

object ViewAnimations {

    fun enterFromBelow(view: View, translationDp: Float, durationMs: Long, delayMs: Long) {
        val distance = view.resources.displayMetrics.density * translationDp
        view.alpha = 0f
        view.translationY = distance
        view.animate()
            .alpha(1f)
            .translationY(0f)
            .setStartDelay(delayMs)
            .setDuration(durationMs)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }

    fun popIn(view: View, durationMs: Long, delayMs: Long) {
        view.alpha = 0f
        view.scaleX = 0.96f
        view.scaleY = 0.96f
        view.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setStartDelay(delayMs)
            .setDuration(durationMs)
            .setInterpolator(OvershootInterpolator(1.1f))
            .start()
    }

    fun expandRule(view: View, durationMs: Long, delayMs: Long) {
        view.pivotX = 0f
        view.scaleX = 0f
        view.animate()
            .scaleX(1f)
            .setStartDelay(delayMs)
            .setDuration(durationMs)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }

    fun revealRibbon(view: View, durationMs: Long, delayMs: Long) {
        view.pivotY = 0f
        view.scaleY = 0f
        view.animate()
            .scaleY(1f)
            .setStartDelay(delayMs)
            .setDuration(durationMs)
            .setInterpolator(OvershootInterpolator(1.4f))
            .start()
    }

    fun crossFade(view: View, durationMs: Long) {
        view.alpha = 0.35f
        view.animate()
            .alpha(1f)
            .setStartDelay(0L)
            .setDuration(durationMs)
            .start()
    }

    fun refreshValue(view: View, durationMs: Long) {
        val distance = view.resources.displayMetrics.density * 6f
        view.alpha = 0.4f
        view.translationY = distance
        view.animate()
            .alpha(1f)
            .translationY(0f)
            .setStartDelay(0L)
            .setDuration(durationMs)
            .start()
    }

    fun pressFeedback(view: View) {
        view.animate().cancel()
        view.animate()
            .scaleX(GameConfig.PRESS_SCALE)
            .scaleY(GameConfig.PRESS_SCALE)
            .setDuration(GameConfig.PRESS_DOWN_MS)
            .withEndAction {
                try {
                    if (!view.isAttachedToWindow) return@withEndAction
                    view.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(GameConfig.PRESS_UP_MS)
                        .start()
                } catch (e: Exception) {
                }
            }
            .start()
    }

    fun traitPop(view: View) {
        view.animate().cancel()
        view.animate()
            .scaleX(GameConfig.TRAIT_SCALE)
            .scaleY(GameConfig.TRAIT_SCALE)
            .setDuration(GameConfig.TRAIT_POP_DOWN_MS)
            .withEndAction {
                try {
                    if (!view.isAttachedToWindow) return@withEndAction
                    view.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(GameConfig.TRAIT_POP_MS)
                        .start()
                } catch (e: Exception) {
                }
            }
            .start()
    }

    fun cancel(vararg views: View) {
        views.forEach { target ->
            try {
                target.animate().cancel()
            } catch (e: Exception) {
            }
        }
    }
}
