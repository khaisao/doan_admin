package com.example.baseproject.ui.student.schedule

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentScheduleStudentBinding
import com.example.baseproject.databinding.FragmentScheduleTeacherBinding
import com.example.baseproject.model.Course
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.teacher.schedule.adapter.CourseTeacher
import com.example.baseproject.util.BundleKey
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleStudentFragment :
    BaseFragment<FragmentScheduleStudentBinding, ScheduleStudentViewModel>(R.layout.fragment_schedule_student) {
    private val viewModel: ScheduleStudentViewModel by viewModels()

    override fun getVM(): ScheduleStudentViewModel = viewModel

    private lateinit var adapter: CourseTeacher

    @Inject
    lateinit var rxPreferences: RxPreferences

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        adapter = CourseTeacher(onCourseClick = {
            val bundle = Bundle()
            bundle.putInt(BundleKey.COURSE_PER_CYCLE_ID, 1)
//            appNavigation.openScheduleToDetailCourse(bundle)
        })
        binding.rvCouse.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvCouse.adapter = adapter

        binding.tvTitle.text = "Hello, " + rxPreferences.getUserName()

        val listCourse = listOf(
            Course(
                id = 6274,
                name = "Evan Torres",
                startTime = "error",
                endTime = "idque",
                teacher = "saperet"
            ),
            Course(
                id = 2345,
                name = "Estelle Waller",
                startTime = "urbanitas",
                endTime = "voluptatum",
                teacher = "tota"
            ),
            Course(
                id = 4197,
                name = "Kristine Burks",
                startTime = "pretium",
                endTime = "dico",
                teacher = "habemus"
            ),
            Course(
                id = 6274,
                name = "Evan Torres",
                startTime = "error",
                endTime = "idque",
                teacher = "saperet"
            ),
            Course(
                id = 2345,
                name = "Estelle Waller",
                startTime = "urbanitas",
                endTime = "voluptatum",
                teacher = "tota"
            ),
            Course(
                id = 4197,
                name = "Kristine Burks",
                startTime = "pretium",
                endTime = "dico",
                teacher = "habemus"
            ),
            Course(
                id = 6274,
                name = "Evan Torres",
                startTime = "error",
                endTime = "idque",
                teacher = "saperet"
            ),
            Course(
                id = 2345,
                name = "Estelle Waller",
                startTime = "urbanitas",
                endTime = "voluptatum",
                teacher = "tota"
            ),
            Course(
                id = 4197,
                name = "Kristine Burks",
                startTime = "pretium",
                endTime = "dico",
                teacher = "habemus"
            )
        )

        adapter.submitList(listCourse)

    }

}