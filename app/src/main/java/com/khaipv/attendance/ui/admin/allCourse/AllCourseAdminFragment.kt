package com.khaipv.attendance.ui.admin.allCourse

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.FragmentScheduleAdminBinding
import com.khaipv.attendance.ui.admin.allCourse.adapter.CourseAdapter
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.setOnSafeClickListener
import com.example.core.utils.toastMessage
import com.khaipv.attendance.model.CourseHaveShedule
import com.khaipv.attendance.model.DetailCourseTeacherAssign
import com.khaipv.attendance.model.OverViewCourseHaveShedule
import com.khaipv.attendance.navigation.AppNavigation
import com.khaipv.attendance.ui.teacher.allCourse.AllCycleTeacherPopupWindow
import com.khaipv.attendance.util.BundleKey
import com.khaipv.attendance.util.DateFormat
import com.khaipv.attendance.util.toDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class AllCourseAdminFragment :
    BaseFragment<FragmentScheduleAdminBinding, AllCourseAdminViewModel>(R.layout.fragment_schedule_admin) {

    private val viewModel: AllCourseAdminViewModel by viewModels()

    override fun getVM(): AllCourseAdminViewModel = viewModel

    private lateinit var adapter: CourseAdapter
    private lateinit var allCycleAdminPopupWindow: AllCycleAdminPopupWindow

    @Inject
    lateinit var appNavigation: AppNavigation

    @Inject
    lateinit var rxPreferences: RxPreferences
    private var currentOriginList: List<CourseHaveShedule> = emptyList()

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        adapter = CourseAdapter(onCourseClick = {
            val bundle = Bundle()
            bundle.putInt(BundleKey.COURSE_ID_TO_GET_SCHEDULE, it.coursePerCycleId)
            appNavigation.openAdminTopToSchedule(bundle)
        })

        binding.rvCourse.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvCourse.adapter = adapter

        binding.tvTitle.text = "Hello, " + rxPreferences.getName()

        viewModel.getAllCourseHaveSchedule()

        allCycleAdminPopupWindow = AllCycleAdminPopupWindow(requireContext()) { cycleClicked ->
            binding.tvAllCourse.text = cycleClicked.cyclesDes
            val item = viewModel.listCourseHaveShedule.value.first { allCycle ->
                allCycle.cycleId == cycleClicked.cycleId
            }
            currentOriginList = item.listCourse
            adapter.submitList(item.listCourse)
            binding.edtSearch.text.clear()
        }

        binding.edtSearch.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrBlank()) {
                adapter.submitList(currentOriginList)
            } else {
                adapter.submitList(currentOriginList.filter { course ->
                    course.courseName.lowercase(Locale.ROOT)
                        .contains(text.toString().lowercase(Locale.ROOT))
                })
            }
        }
    }

    override fun setOnClick() {
        super.setOnClick()
        binding.tvAllCourse.setOnSafeClickListener {
            allCycleAdminPopupWindow.showPopup(binding.tvAllCourse)
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.listCourseHaveShedule.collectFlowOnView(viewLifecycleOwner) {
                allCycleAdminPopupWindow.setData(it)
                for (item in it) {
                    val cyclesStartDate = item.cycleStartDate.toDate(DateFormat.FORMAT_1)
                    val cyclesEndDate = item.cycleEndDate.toDate(DateFormat.FORMAT_1)
                    val currentTime = Date()
                    if (currentTime.after(cyclesStartDate) && currentTime.before(cyclesEndDate)) {
                        adapter.submitList(item.listCourse)
                        currentOriginList = item.listCourse
                        binding.tvAllCourse.text = item.cyclesDes
                        break
                    }
                }
            }
        }
    }
}