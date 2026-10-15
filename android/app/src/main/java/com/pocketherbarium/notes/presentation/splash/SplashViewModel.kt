package com.pocketherbarium.notes.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketherbarium.notes.core.config.GameConfig
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val _handOffReady = MutableStateFlow(false)
    val handOffReady: StateFlow<Boolean> = _handOffReady.asStateFlow()

    private val _indicatorVisible = MutableStateFlow(true)
    val indicatorVisible: StateFlow<Boolean> = _indicatorVisible.asStateFlow()

    private var timerJob: Job? = null

    init {
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            delay(GameConfig.LOADER_DURATION_MS)
            _indicatorVisible.value = false
            _handOffReady.value = true
        }
    }

    fun consumeHandOff() {
        _handOffReady.value = false
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        timerJob = null
    }
}
