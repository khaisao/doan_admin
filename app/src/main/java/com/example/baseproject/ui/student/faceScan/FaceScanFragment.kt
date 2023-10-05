package com.example.baseproject.ui.student.faceScan

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentFaceScanBinding
import com.example.baseproject.databinding.FragmentScheduleStudentBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.student.schedule.adapter.CourseStudentRegisterAdapter
import com.example.baseproject.util.BundleKey
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.collectFlowOnView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class FaceScanFragment :
    BaseFragment<FragmentFaceScanBinding, FaceScanViewModel>(R.layout.fragment_face_scan) {
    private val viewModel: FaceScanViewModel by viewModels()

    override fun getVM(): FaceScanViewModel = viewModel


    @Inject
    lateinit var rxPreferences: RxPreferences

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

    }

    override fun bindingStateView() {
        super.bindingStateView()

    }

}