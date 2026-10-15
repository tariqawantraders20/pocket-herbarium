package com.pocketherbarium.notes.presentation.splash

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.pocketherbarium.notes.core.config.GameConfig
import com.pocketherbarium.notes.databinding.FragmentSplashBinding

class SplashAnimator(private val binding: FragmentSplashBinding) {

    private val loopingAnimators = mutableListOf<Animator>()
    private val entranceTargets = mutableListOf<View>()
    private var started = false

    fun start() {
        if (started) return
        started = true
        startPlatePulse()
        startLeafSway(binding.splashLeafLeft, GameConfig.SPLASH_SWAY_MS, SWAY_START)
        startLeafSway(binding.splashLeafRight, GameConfig.SPLASH_SWAY_ALT_MS, SWAY_START_ALT)
        startCaptionBreath()
        playTitleEntrance()
        playSubtitleEntrance()
    }

    fun cancel() {
        started = false
        loopingAnimators.forEach { animator ->
            try {
                animator.cancel()
            } catch (e: Exception) {
            }
        }
        loopingAnimators.clear()
        entranceTargets.forEach { target ->
            try {
                target.animate().cancel()
            } catch (e: Exception) {
            }
        }
        entranceTargets.clear()
    }

    private fun startPlatePulse() {
        val scaleX = ObjectAnimator.ofFloat(
            binding.splashPlate,
            View.SCALE_X,
            PLATE_MIN_SCALE,
            PLATE_MAX_SCALE
        )
        val scaleY = ObjectAnimator.ofFloat(
            binding.splashPlate,
            View.SCALE_Y,
            PLATE_MIN_SCALE,
            PLATE_MAX_SCALE
        )
        listOf(scaleX, scaleY).forEach { animator ->
            animator.duration = GameConfig.SPLASH_PULSE_MS
            animator.repeatCount = ValueAnimator.INFINITE
            animator.repeatMode = ValueAnimator.REVERSE
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.start()
            loopingAnimators.add(animator)
        }
    }

    private fun startLeafSway(target: View, durationMs: Long, startRotation: Float) {
        val sway = ObjectAnimator.ofFloat(target, View.ROTATION, startRotation, -startRotation)
        sway.duration = durationMs
        sway.repeatCount = ValueAnimator.INFINITE
        sway.repeatMode = ValueAnimator.REVERSE
        sway.interpolator = AccelerateDecelerateInterpolator()
        sway.start()
        loopingAnimators.add(sway)

        val drift = ObjectAnimator.ofFloat(
            target,
            View.TRANSLATION_Y,
            -DRIFT_DISTANCE,
            DRIFT_DISTANCE
        )
        drift.duration = durationMs + DRIFT_OFFSET_MS
        drift.repeatCount = ValueAnimator.INFINITE
        drift.repeatMode = ValueAnimator.REVERSE
        drift.interpolator = AccelerateDecelerateInterpolator()
        drift.start()
        loopingAnimators.add(drift)
    }

    private fun startCaptionBreath() {
        val breath = ObjectAnimator.ofFloat(
            binding.splashCaption,
            View.ALPHA,
            CAPTION_MIN_ALPHA,
            CAPTION_MAX_ALPHA
        )
        breath.duration = GameConfig.SPLASH_PULSE_MS
        breath.repeatCount = ValueAnimator.INFINITE
        breath.repeatMode = ValueAnimator.REVERSE
        breath.interpolator = AccelerateDecelerateInterpolator()
        breath.start()
        loopingAnimators.add(breath)
    }

    private fun playTitleEntrance() {
        val distance = binding.root.resources.displayMetrics.density *
            GameConfig.TITLE_TRANSLATION_DP
        listOf(binding.splashTitleTop, binding.splashTitleBottom).forEachIndexed { index, target ->
            target.alpha = 0f
            target.translationY = distance
            target.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(GameConfig.SPLASH_TITLE_DELAY_MS + index * GameConfig.STAGGER_MS)
                .setDuration(GameConfig.SPLASH_TITLE_MS)
                .setInterpolator(DecelerateInterpolator())
                .start()
            entranceTargets.add(target)
        }
    }

    private fun playSubtitleEntrance() {
        val target = binding.splashSubtitle
        target.alpha = 0f
        target.animate()
            .alpha(1f)
            .setStartDelay(GameConfig.SPLASH_TITLE_DELAY_MS + SUBTITLE_EXTRA_DELAY_MS)
            .setDuration(GameConfig.SPLASH_TITLE_MS)
            .setInterpolator(DecelerateInterpolator())
            .start()
        entranceTargets.add(target)
    }

    companion object {
        private const val PLATE_MIN_SCALE = 0.94f
        private const val PLATE_MAX_SCALE = 1.0f
        private const val SWAY_START = -6f
        private const val SWAY_START_ALT = 6f
        private const val DRIFT_DISTANCE = 10f
        private const val DRIFT_OFFSET_MS = 700L
        private const val CAPTION_MIN_ALPHA = 0.45f
        private const val CAPTION_MAX_ALPHA = 1.0f
        private const val SUBTITLE_EXTRA_DELAY_MS = 260L
    }
}
