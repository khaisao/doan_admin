package com.khaipv.attendance.ui.teacher.editSchedule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import com.arkapp.iosdatettimepicker.ui.DialogDateTimePicker
import com.arkapp.iosdatettimepicker.utils.OnDateTimeSelectedListener
import com.khaipv.attendance.R
import com.khaipv.attendance.navigation.AppNavigation
import com.khaipv.attendance.util.BundleKey
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.setOnSafeClickListener
import com.example.core.utils.toastMessage
import com.khaipv.attendance.databinding.FragmentEditScheduleTeacherBinding
import com.khaipv.attendance.model.DetailScheduleCourse
import com.khaipv.attendance.model.UpdateScheduleBody
import com.khaipv.attendance.ui.teacher.allCourse.AllCycleTeacherPopupWindow
import com.khaipv.attendance.util.DateFormat
import com.khaipv.attendance.util.parcelable
import com.khaipv.attendance.util.toDate
import com.khaipv.attendance.util.toDateWithFormatInputAndOutPut
import com.khaipv.attendance.util.toStringWithFormat
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class EditScheduleTeacherFragment :
    BaseFragment<FragmentEditScheduleTeacherBinding, EditScheduleTeacherViewModel>(R.layout.fragment_edit_schedule_teacher) {

    private val viewModel: EditScheduleTeacherViewModel by viewModels()

    override fun getVM(): EditScheduleTeacherViewModel = viewModel

    private var schedule: DetailScheduleCourse? = null

    @Inject
    lateinit var appNavigation: AppNavigation

    private var startTime: Date? = null
    private var endTime: Date? = null
    private var classroomId: Int? = null

    private lateinit var allClassroomTeacherPopupWindow: AllClassroomTeacherPopupWindow


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        schedule = arguments?.parcelable(BundleKey.ITEM_SCHEDULE_TO_EDIT)
        if (schedule != null) {
            startTime =
                schedule!!.startTime.toDate(DateFormat.FORMAT_1) ?: Calendar.getInstance().time
            endTime = schedule!!.endTime.toDate(DateFormat.FORMAT_1) ?: Calendar.getInstance().time
            classroomId = schedule!!.classroomId
        }

        viewModel.getAllClassroom()

        binding.tvStartTime.text =
            schedule?.startTime?.toDateWithFormatInputAndOutPut(
                DateFormat.FORMAT_1,
                DateFormat.FORMAT_5
            )
                ?: ""
        binding.tvEndTime.text = schedule?.endTime?.toDateWithFormatInputAndOutPut(
            DateFormat.FORMAT_1,
            DateFormat.FORMAT_5
        )
            ?: ""
        binding.tvClassroom.text = schedule?.classroomName ?: ""

        allClassroomTeacherPopupWindow =
            AllClassroomTeacherPopupWindow(requireContext()) { classroom ->
                binding.tvClassroom.text = classroom.name
                classroomId = classroom.classroomId
            }

    }

    override fun setOnClick() {
        super.setOnClick()

        binding.llStartTime.setOnSafeClickListener {
            showDateTimePickerForStartTimeDialog()
        }

        binding.llEndTime.setOnSafeClickListener {
            showDateTimePickerForEndTimeDialog()
        }

        binding.llClassroom.setOnSafeClickListener {
            allClassroomTeacherPopupWindow.showPopup(binding.llClassroom)
        }

        binding.tvSubmit.setOnSafeClickListener {
            if (schedule != null && startTime != null && endTime != null && classroomId != null) {
                val updateScheduleBody = UpdateScheduleBody(
                    classroomId = classroomId!!,
                    startTime = startTime!!.toStringWithFormat(DateFormat.FORMAT_6),
                    endTime = endTime!!.toStringWithFormat(DateFormat.FORMAT_6),
                    scheduleId = schedule!!.scheduleId
                )
                viewModel.updateSchedule(updateScheduleBody)
            }

        }
    }

    private fun showDateTimePickerForStartTimeDialog() {
        if (schedule != null) {
            try {
                val startDate: Calendar = Calendar.getInstance()
                startDate.time = startTime

                val dateTimeSelectedListener = object :
                    OnDateTimeSelectedListener {
                    override fun onDateTimeSelected(selectedDateTime: Calendar) {
                        startTime = selectedDateTime.time
                        binding.tvStartTime.text =
                            selectedDateTime.time.toStringWithFormat(DateFormat.FORMAT_5)
                    }
                }

                val dateTimePickerDialog = DialogDateTimePicker(
                    requireContext(),
                    startDate,
                    12,
                    dateTimeSelectedListener,
                    "Select date and time"
                )

                dateTimePickerDialog.show()
            } catch (e: Exception) {
                toastMessage("Error, try again")
            }
        }
    }

    private fun showDateTimePickerForEndTimeDialog() {
        if (schedule != null) {
            try {
                val startDate: Calendar = Calendar.getInstance()
                startDate.time = endTime

                val dateTimeSelectedListener = object :
                    OnDateTimeSelectedListener {
                    override fun onDateTimeSelected(selectedDateTime: Calendar) {
                        endTime = selectedDateTime.time
                        binding.tvEndTime.text =
                            selectedDateTime.time.toStringWithFormat(DateFormat.FORMAT_5)
                    }
                }

                val dateTimePickerDialog = DialogDateTimePicker(
                    requireContext(),
                    startDate,
                    12,
                    dateTimeSelectedListener,
                    "Select date and time"
                )

                dateTimePickerDialog.show()
            } catch (e: Exception) {
                toastMessage("Error, try again")
            }
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        viewModel.allClassroom.collectFlowOnView(viewLifecycleOwner) {
            allClassroomTeacherPopupWindow.setData(it)
        }

        viewModel.isUpdateScheduleSuccess.collectFlowOnView(viewLifecycleOwner) {
            if (it != null && it == true) {
                val bundle = Bundle()
                bundle.putString(BundleKey.TITLE_ACTION_SUCCESS, "Update schedule success")
                bundle.putString(
                    BundleKey.DES_ACTION_SUCCESS,
                    "Congratulation! Update schedule success. Please continue your work"
                )
                appNavigation.openEditScheduleToEditScheduleSuccess(bundle)
            }
        }
    }

}