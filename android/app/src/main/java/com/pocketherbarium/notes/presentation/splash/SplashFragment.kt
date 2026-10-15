package com.pocketherbarium.notes.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.pocketherbarium.notes.MainActivity
import com.pocketherbarium.notes.R
import com.pocketherbarium.notes.databinding.FragmentSplashBinding
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding: FragmentSplashBinding
        get() = checkNotNull(_binding)

    private val viewModel: SplashViewModel by viewModels()

    private var animator: SplashAnimator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        describeIndicator()
        animator = SplashAnimator(binding).also { it.start() }
        observeHandOff()
        observeIndicator()
    }

    private fun describeIndicator() {
        ViewCompat.setStateDescription(
            binding.splashIndicator,
            getString(R.string.splash_caption)
        )
    }

    private fun observeHandOff() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.handOffReady.collect { ready ->
                    if (ready) {
                        handOff()
                    }
                }
            }
        }
    }

    private fun observeIndicator() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.indicatorVisible.collect { visible ->
                    _binding?.splashIndicator?.isVisible = visible
                }
            }
        }
    }

    private fun handOff() {
        if (!isAdded) return
        if (_binding == null) return
        val host = activity as? MainActivity ?: return
        if (!host.navigator.showMenu()) return
        viewModel.consumeHandOff()
    }

    override fun onDestroyView() {
        animator?.cancel()
        animator = null
        super.onDestroyView()
        _binding = null
    }
}
