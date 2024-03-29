package com.khaipv.attendance.ui.teacher.allCourse

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.FragmentScheduleTeacherBinding
import com.khaipv.attendance.model.DetailCourseTeacherAssign
import com.khaipv.attendance.navigation.AppNavigation
import com.khaipv.attendance.shareData.ShareViewModel
import com.khaipv.attendance.ui.teacher.allCourse.adapter.CourseTeacherAssignAdapter
import com.khaipv.attendance.util.BundleKey
import com.khaipv.attendance.util.DateFormat
import com.khaipv.attendance.util.toDate
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.loadImage
import com.example.core.utils.setOnSafeClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class AllCourseTeacherFragment :
    BaseFragment<FragmentScheduleTeacherBinding, AllCourseTeacherViewModel>(R.layout.fragment_schedule_teacher) {

    private val viewModel: AllCourseTeacherViewModel by viewModels()

    override fun getVM(): AllCourseTeacherViewModel = viewModel

    private val shareViewModel: ShareViewModel by activityViewModels()

    private lateinit var adapter: CourseTeacherAssignAdapter

    @Inject
    lateinit var rxPreferences: RxPreferences

    @Inject
    lateinit var appNavigation: AppNavigation

    private var currentOriginList: List<DetailCourseTeacherAssign> = emptyList()

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        adapter = CourseTeacherAssignAdapter(onCourseClick = {
            val bundle = Bundle()
            bundle.putInt(BundleKey.COURSE_ID_TO_GET_SCHEDULE, it.coursePerCycleId)
            appNavigation.openScheduleToDetailCourse(bundle)
        }, rxPreferences)
        binding.rvCourse.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvCourse.adapter = adapter

        binding.tvTitle.text = "Hello, " + rxPreferences.getName()

        binding.ivAvatar.loadImage(rxPreferences.getAvatar())

        viewModel.getAllCourseAssign()

        binding.edtSearch.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrBlank()) {
                adapter.submitList(currentOriginList)
            } else {
                adapter.submitList(currentOriginList.filter { course ->
                    course.courseName.lowercase(Locale.ROOT)
                        .contains(text.toString().lowercase(Locale.ROOT))
                })
            }
        }

        allCycleTeacherPopupWindow = AllCycleTeacherPopupWindow(requireContext()) { cycleClicked ->
            binding.tvAllCourse.text = cycleClicked.cyclesDes
            val item = viewModel.allCourseTeacherAssign.value.first { allCycle ->
                allCycle.cycleId == cycleClicked.cycleId
            }
            currentOriginList = item.listCourse
            adapter.submitList(item.listCourse)
            binding.edtSearch.text.clear()
        }
    }

    private lateinit var allCycleTeacherPopupWindow: AllCycleTeacherPopupWindow

    override fun setOnClick() {
        super.setOnClick()

        binding.ivAvatar.setOnSafeClickListener {
            shareViewModel.setPositionBottomNav(1)
        }

        binding.tvAllCourse.setOnSafeClickListener {
            allCycleTeacherPopupWindow.showPopup(binding.tvAllCourse)
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.allCourseTeacherAssign.collectFlowOnView(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    allCycleTeacherPopupWindow.setData(it)
                    var isCurrentInCycle = false
                    for (item in it) {
                        val cyclesStartDate = item.cycleStartDate.toDate(DateFormat.FORMAT_1)
                        val cyclesEndDate = item.cycleEndDate.toDate(DateFormat.FORMAT_1)
                        val currentTime = Date()
                        if (currentTime.after(cyclesStartDate) && currentTime.before(cyclesEndDate)) {
                            isCurrentInCycle = true
                            if (item.listCourse.isNotEmpty()) {
                                adapter.submitList(item.listCourse)
                                currentOriginList = item.listCourse
                            }
                            binding.tvAllCourse.text = item.cyclesDes
                            break
                        }
                    }
                    if (!isCurrentInCycle && it.last().listCourse.isNotEmpty()) {
                        adapter.submitList(it.last().listCourse)
                        currentOriginList = it.last().listCourse
                        binding.tvAllCourse.text = it.last().cyclesDes
                    }
                }
            }
        }
    }
}