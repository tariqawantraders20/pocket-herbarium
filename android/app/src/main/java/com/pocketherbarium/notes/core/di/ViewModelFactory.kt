package com.pocketherbarium.notes.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pocketherbarium.notes.presentation.game.GameViewModel
import com.pocketherbarium.notes.presentation.gameover.GameOverViewModel
import com.pocketherbarium.notes.presentation.menu.MenuViewModel
import com.pocketherbarium.notes.presentation.splash.SplashViewModel

class ViewModelFactory(
    private val locator: ServiceLocator,
    private val plantId: Long = NO_PLANT
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(SplashViewModel::class.java) ->
            SplashViewModel() as T

        modelClass.isAssignableFrom(MenuViewModel::class.java) ->
            MenuViewModel(
                locator.getPlants,
                locator.getCollections,
                locator.getSettings,
                locator.resetLibrary
            ) as T

        modelClass.isAssignableFrom(GameViewModel::class.java) ->
            GameViewModel(
                locator.savePlant,
                locator.getSettings,
                locator.setActiveCollection
            ) as T

        modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
            GameOverViewModel(
                locator.getPlants,
                locator.getCollections,
                plantId
            ) as T

        else -> throw IllegalArgumentException("Unsupported ViewModel: " + modelClass.name)
    }

    companion object {
        const val NO_PLANT = -1L
    }
}
