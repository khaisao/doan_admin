package com.example.baseproject.ui.teacher.allAttendance

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentAllAttendanceBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.util.BundleKey
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.toastMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AllAttendanceFragment :
    BaseFragment<FragmentAllAttendanceBinding, AllAttendanceViewModel>(R.layout.fragment_all_attendance) {

    private val viewModel: AllAttendanceViewModel by viewModels()

    override fun getVM(): AllAttendanceViewModel = viewModel

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        val courseId = arguments?.getInt(BundleKey.COURSE_PER_CYCLE_ID_ALL_ATTENDANCE)
        if (courseId != null) {
            viewModel.getAllAttendanceSpecificCourse(courseId)
        } else {
            toastMessage("Error, try again")
        }

    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.allAttendance.collectFlowOnView(viewLifecycleOwner) {

            }
        }
    }


}