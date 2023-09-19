package com.example.baseproject.ui.admin.schedule

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentScheduleAdminBinding
import com.example.baseproject.model.Course
import com.example.baseproject.ui.admin.schedule.adapter.CourseAdapter
import com.example.core.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleAdminFragment :
    BaseFragment<FragmentScheduleAdminBinding, ScheduleAdminViewModel>(R.layout.fragment_schedule_admin) {
    private val viewModel: ScheduleAdminViewModel by viewModels()

    override fun getVM(): ScheduleAdminViewModel = viewModel

    private lateinit var adapter: CourseAdapter

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        adapter = CourseAdapter()
        binding.rvCouse.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvCouse.adapter = adapter
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