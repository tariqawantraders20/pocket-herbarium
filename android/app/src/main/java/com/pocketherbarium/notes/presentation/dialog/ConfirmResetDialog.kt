package com.pocketherbarium.notes.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.pocketherbarium.notes.R
import com.pocketherbarium.notes.core.util.ViewAnimations
import com.pocketherbarium.notes.databinding.DialogConfirmResetBinding

class ConfirmResetDialog : DialogFragment() {

    private var _binding: DialogConfirmResetBinding? = null
    private val binding: DialogConfirmResetBinding
        get() = checkNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_PocketHerbarium_Dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogConfirmResetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.resetCancel.setOnClickListener { dismissAllowingStateLoss() }
        binding.resetConfirm.setOnClickListener { target ->
            ViewAnimations.pressFeedback(target)
            parentFragmentManager.setFragmentResult(REQUEST_KEY, Bundle())
            dismissAllowingStateLoss()
        }
    }

    override fun onDestroyView() {
        _binding?.let { safe -> ViewAnimations.cancel(safe.resetConfirm, safe.resetCancel) }
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "confirm_reset_dialog"
        const val REQUEST_KEY = "request_reset_library"
    }
}
