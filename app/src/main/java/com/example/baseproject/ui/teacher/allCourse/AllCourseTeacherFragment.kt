package com.example.baseproject.ui.teacher.allCourse

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentScheduleTeacherBinding
import com.example.baseproject.model.DetailCourseTeacherAssign
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.shareData.ShareViewModel
import com.example.baseproject.ui.teacher.allCourse.adapter.CourseTeacherAssignAdapter
import com.example.baseproject.util.BundleKey
import com.example.baseproject.util.DateFormat
import com.example.baseproject.util.toDate
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
        })
        binding.rvCouse.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvCouse.adapter = adapter

        binding.tvTitle.text = "Hello, " + rxPreferences.getUserName()

        binding.ivAvatar.loadImage(rxPreferences.getAvatar())

        viewModel.getAllCourseAssign()

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

        allCyclePopupWindow = AllCyclePopupWindow(requireContext()) { cycleClicked ->
            binding.tvAllCourse.text = cycleClicked.cyclesDes
            val item = viewModel.allCourseTeacherAssign.value.first { allCycle ->
                allCycle.cycleId == cycleClicked.cycleId
            }
            currentOriginList = item.listCourse
            adapter.submitList(item.listCourse)
            binding.edtSearch.text.clear()
        }
    }

    private lateinit var allCyclePopupWindow: AllCyclePopupWindow

    override fun setOnClick() {
        super.setOnClick()

        binding.ivAvatar.setOnSafeClickListener {
            shareViewModel.setPositionBottomNav(1)
        }

        binding.tvAllCourse.setOnSafeClickListener {
            allCyclePopupWindow.showPopup(binding.tvAllCourse)
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.allCourseTeacherAssign.collectFlowOnView(viewLifecycleOwner) {
                allCyclePopupWindow.setData(it)
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