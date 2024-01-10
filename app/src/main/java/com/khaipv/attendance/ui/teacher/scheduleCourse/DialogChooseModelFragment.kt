package com.khaipv.attendance.ui.teacher.scheduleCourse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.khaipv.attendance.databinding.FragmentDialogNoticeEmptyImageProfileBinding
import com.example.core.utils.setOnSafeClickListener
import com.khaipv.attendance.databinding.FragmentDialogChooseModelBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogChooseModelFragment(
    private val onNavigateToFacenetScreen: () -> Unit,
    private val onNavigateToKbyScreen: () -> Unit
) : DialogFragment() {
    private lateinit var binding: FragmentDialogChooseModelBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogChooseModelBinding.inflate(inflater)
        isCancelable = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvStartScanFace.setOnSafeClickListener {
            dismiss()
            if (binding.rbFacenet.isChecked) {
                onNavigateToFacenetScreen.invoke()
            }
            if (binding.rbKby.isChecked) {
                onNavigateToKbyScreen.invoke()
            }
        }
    }
}