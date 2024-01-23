package com.khaipv.attendance.ui.student.faceScan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.khaipv.attendance.databinding.FragmentDialogLookCameraBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogLookTheCameraFragment : DialogFragment() {
    private lateinit var binding: FragmentDialogLookCameraBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogLookCameraBinding.inflate(inflater)
        isCancelable = false
        return binding.root
    }

}