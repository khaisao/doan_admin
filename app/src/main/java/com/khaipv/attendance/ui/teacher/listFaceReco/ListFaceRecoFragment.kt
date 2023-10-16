package com.khaipv.attendance.ui.teacher.listFaceReco

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.FragmentListFaceRecoBinding
import com.khaipv.attendance.model.AttendanceBody
import com.khaipv.attendance.navigation.AppNavigation
import com.khaipv.attendance.shareData.ShareViewModel
import com.khaipv.attendance.ui.teacher.listFaceReco.adapter.ListFaceRecoAdapter
import com.khaipv.attendance.util.BundleKey
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.toastMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ListFaceRecoFragment :
    BaseFragment<FragmentListFaceRecoBinding, ListFaceRecoViewModel>(R.layout.fragment_list_face_reco) {
    private val viewModel: ListFaceRecoViewModel by viewModels()
    private val shareViewModel: ShareViewModel by activityViewModels()

    override fun getVM(): ListFaceRecoViewModel = viewModel

    private val adapter: ListFaceRecoAdapter = ListFaceRecoAdapter()

    private var sheduleId: Int? = null

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        binding.rvData.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvData.adapter = adapter
        sheduleId = arguments?.getInt(BundleKey.SCHEDULE_ID_ATTENDANCE)
        if (sheduleId == null) {
            toastMessage("Error, try again")
            appNavigation.navigateUp()
        }
    }

    override fun setOnClick() {
        super.setOnClick()
        binding.tvAttendance.setOnClickListener {
            val listAttendanceBody = mutableListOf<AttendanceBody>()
            for (item in shareViewModel.listStudentRecognized.value) {
                if (item.isReco) {
                    listAttendanceBody.add(
                        AttendanceBody(
                            item.studentId,
                            item.registrationId,
                            sheduleId!!
                        )
                    )
                }
            }
            viewModel.attendance(listAttendanceBody)
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            shareViewModel.listStudentRecognized.collectFlowOnView(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }
        lifecycleScope.launch {
            viewModel.attendanceActionStateFlow.collectFlowOnView(viewLifecycleOwner) {
                if (it is AttendanceEvent.AttendanceSuccess) {
                    toastMessage("Success")
                } else {
                    toastMessage("Error, please check again")
                }
            }
        }
    }
}