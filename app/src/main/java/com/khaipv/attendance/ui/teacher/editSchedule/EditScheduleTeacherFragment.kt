package com.khaipv.attendance.ui.teacher.editSchedule

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.arkapp.iosdatettimepicker.ui.DialogDateTimePicker
import com.arkapp.iosdatettimepicker.utils.OnDateTimeSelectedListener
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.FragmentDetailCourseBinding
import com.khaipv.attendance.navigation.AppNavigation
import com.khaipv.attendance.util.BundleKey
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.setOnSafeClickListener
import com.example.core.utils.toastMessage
import com.khaipv.attendance.databinding.FragmentEditScheduleTeacherBinding
import com.khaipv.attendance.model.DetailScheduleCourse
import com.khaipv.attendance.util.DateFormat
import com.khaipv.attendance.util.parcelable
import com.khaipv.attendance.util.toDateWithFormatInputAndOutPut
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class EditScheduleTeacherFragment :
    BaseFragment<FragmentEditScheduleTeacherBinding, EditScheduleTeacherViewModel>(R.layout.fragment_edit_schedule_teacher) {

    private val viewModel: EditScheduleTeacherViewModel by viewModels()

    override fun getVM(): EditScheduleTeacherViewModel = viewModel

    private var schedule: DetailScheduleCourse? = null

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        schedule = arguments?.parcelable(BundleKey.ITEM_SCHEDULE_TO_EDIT)
        binding.tvStartTime.text =
            schedule?.startTime?.toDateWithFormatInputAndOutPut(
                DateFormat.FORMAT_1,
                DateFormat.FORMAT_4
            )
                ?: ""
        binding.tvEndTime.text = schedule?.endTime?.toDateWithFormatInputAndOutPut(
            DateFormat.FORMAT_1,
            DateFormat.FORMAT_4
        )
            ?: ""
        binding.tvClassroom.text = schedule?.classroomName ?: ""

    }

    override fun setOnClick() {
        super.setOnClick()
        binding.tvStartTime.setOnSafeClickListener {
            showDateTimePickerDialog()
        }
        binding.tvEndTime.setOnSafeClickListener {
            showDateTimePickerDialog()
        }

    }

    private fun showDateTimePickerDialog() {
        val startDate: Calendar = Calendar.getInstance()

        val dateTimeSelectedListener = object :

            OnDateTimeSelectedListener {
            override fun onDateTimeSelected(selectedDateTime: Calendar) {
                //This is the calendar reference of selected date and time.
                //We can format the date time as we need here.
                println("Selected date ${selectedDateTime.time}")

            }
        }

        val dateTimePickerDialog = DialogDateTimePicker(

            requireContext(), //context
            startDate, //start date of calendar
            12, //No. of future months to shown in calendar
            dateTimeSelectedListener,
            "Select date and time"
        ) //Dialog title

        dateTimePickerDialog.show()
    }

    override fun bindingStateView() {
        super.bindingStateView()

    }

}