package com.example.baseproject.ui.student.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.baseproject.databinding.FragmentDialogNoticeEmptyImageProfileBinding
import com.example.core.utils.setOnSafeClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogNoticeEmptyImageProfileFragment(
    private val onNavigateToScanFace: () -> Unit
) : DialogFragment() {
    private lateinit var binding:FragmentDialogNoticeEmptyImageProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogNoticeEmptyImageProfileBinding.inflate(inflater)
        isCancelable = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvStartScanFace.setOnSafeClickListener {
            dismiss()
            onNavigateToScanFace.invoke()
        }
    }
}