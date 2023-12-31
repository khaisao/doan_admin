package com.khaipv.attendance.ui.teacher.detailAttendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.FragmentDetailAttendanceTeacherBinding
import com.khaipv.attendance.databinding.PopUpWindownAllCyclesBinding
import com.khaipv.attendance.databinding.PopUpWindownTypeStudentAttendanceBinding
import com.khaipv.attendance.model.DetailScheduleCourse
import com.khaipv.attendance.navigation.AppNavigation
import com.khaipv.attendance.util.BundleKey
import com.khaipv.attendance.util.DateFormat
import com.khaipv.attendance.util.parcelable
import com.khaipv.attendance.util.toDateWithFormatInputAndOutPut
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.loadImage
import com.example.core.utils.setOnSafeClickListener
import com.example.core.utils.toastMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DetailAttendanceTeacherFragment :
    BaseFragment<FragmentDetailAttendanceTeacherBinding, DetailAttendanceTeacherViewModel>(R.layout.fragment_detail_attendance_teacher) {
    private val viewModel: DetailAttendanceTeacherViewModel by viewModels()

    override fun getVM(): DetailAttendanceTeacherViewModel = viewModel

    private lateinit var adapter: DetailAttendanceTeacherAdapter

    @Inject
    lateinit var rxPreferences: RxPreferences

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        adapter = DetailAttendanceTeacherAdapter()
        binding.rvAttendance.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvAttendance.adapter = adapter

        binding.ivAvatar.loadImage(rxPreferences.getAvatar(), R.drawable.no_avatar)

        val itemSchedule =
            arguments?.parcelable<DetailScheduleCourse>(BundleKey.ITEM_SCHEDULE)

        if (itemSchedule != null) {
            binding.tvClassroomName.text = "Class room: " + itemSchedule.classroomName
            binding.tvDateTime.text =
                buildString {
                    append("Date: ")
                    append(
                        itemSchedule.startTime.toDateWithFormatInputAndOutPut(
                            DateFormat.FORMAT_1,
                            DateFormat.FORMAT_2
                        )
                    )
                }
            binding.tvStartEndTime.text =
                buildString {
                    append("Start/end time: ")
                    append(
                        itemSchedule.startTime.toDateWithFormatInputAndOutPut(
                            DateFormat.FORMAT_1,
                            DateFormat.FORMAT_3
                        )
                    )
                    append(" - ")
                    append(
                        itemSchedule.endTime.toDateWithFormatInputAndOutPut(
                            DateFormat.FORMAT_1,
                            DateFormat.FORMAT_3
                        )
                    )
                }
            viewModel.getAllAttendanceSpecificSchedule(
                itemSchedule.coursePerCycleId,
                itemSchedule.scheduleId
            )
        } else {
            toastMessage("Error, try again")
            appNavigation.navigateUp()
        }

    }

    override fun setOnClick() {
        super.setOnClick()
        binding.tvFilter.setOnSafeClickListener {
            showPopupChooseTypeAccount()
        }
    }

    private fun showPopupChooseTypeAccount() {
        val inflater = LayoutInflater.from(requireContext())
        val popupWindowBinding = PopUpWindownTypeStudentAttendanceBinding.inflate(inflater)

        val popupWindow = PopupWindow(
            popupWindowBinding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindowBinding.tvAllStudent.setOnSafeClickListener {
            adapter.submitList(viewModel.allDetailAttendanceStudent.value)
            binding.tvFilter.text = getString(R.string.all_student)
            popupWindow.dismiss()
        }

        popupWindowBinding.tvCheckedIn.setOnSafeClickListener {
            adapter.submitList(viewModel.allDetailAttendanceStudent.value.filter { it.timeAttendance != null })
            binding.tvFilter.text = getString(R.string.checked_in)
            popupWindow.dismiss()
        }

        popupWindowBinding.tvNotCheckedIn.setOnSafeClickListener {
            adapter.submitList(viewModel.allDetailAttendanceStudent.value.filter { it.timeAttendance == null })
            binding.tvFilter.text = getString(R.string.not_checked_in)
            popupWindow.dismiss()
        }

        popupWindow.showAsDropDown(binding.tvFilter)
    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.allDetailAttendanceStudent.collectFlowOnView(viewLifecycleOwner) {
                adapter.submitList(it)
                binding.tvHeadCount.text =
                    buildString {
                        append("Head count: ")
                        append(it.count { item -> item.timeAttendance != null })
                        append("/")
                        append(it.count())
                    }
            }
        }
    }

}

