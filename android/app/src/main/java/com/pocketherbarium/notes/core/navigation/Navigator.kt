package com.pocketherbarium.notes.core.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.pocketherbarium.notes.R
import com.pocketherbarium.notes.presentation.game.GameFragment
import com.pocketherbarium.notes.presentation.gameover.GameOverFragment
import com.pocketherbarium.notes.presentation.menu.MenuFragment
import com.pocketherbarium.notes.presentation.splash.SplashFragment

class Navigator(
    private val fragmentManager: FragmentManager,
    private val containerId: Int
) {

    fun showSplash(): Boolean {
        if (fragmentManager.isStateSaved) return false
        fragmentManager.beginTransaction()
            .replace(containerId, SplashFragment())
            .commit()
        return true
    }

    fun showMenu(): Boolean {
        if (fragmentManager.isStateSaved) return false
        fade().replace(containerId, MenuFragment()).commit()
        return true
    }

    fun showGame(): Boolean {
        if (fragmentManager.isStateSaved) return false
        slide().replace(containerId, GameFragment()).addToBackStack(TAG_GAME).commit()
        return true
    }

    fun showResult(plantId: Long): Boolean {
        if (fragmentManager.isStateSaved) return false
        val fragment: Fragment = GameOverFragment.newInstance(plantId)
        fragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.fade_in,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .replace(containerId, fragment)
            .addToBackStack(TAG_RESULT)
            .commit()
        return true
    }

    fun restartGame(): Boolean {
        if (fragmentManager.isStateSaved) return false
        fragmentManager.popBackStackImmediate(TAG_GAME, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        return showGame()
    }

    fun backToMenu(): Boolean {
        if (fragmentManager.isStateSaved) return false
        if (fragmentManager.backStackEntryCount == 0) return false
        fragmentManager.popBackStackImmediate(TAG_GAME, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        return true
    }

    private fun fade(): FragmentTransaction = fragmentManager.beginTransaction()
        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)

    private fun slide(): FragmentTransaction = fragmentManager.beginTransaction()
        .setCustomAnimations(
            R.anim.slide_in_right,
            R.anim.slide_out_left,
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )

    companion object {
        const val TAG_GAME = "game"
        const val TAG_RESULT = "result"
    }
}
