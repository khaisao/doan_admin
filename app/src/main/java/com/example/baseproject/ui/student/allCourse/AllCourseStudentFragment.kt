package com.example.baseproject.ui.student.allCourse

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentAllCourseStudentBinding
import com.example.baseproject.model.DetailCourseStudentRegister
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.shareData.ShareViewModel
import com.example.baseproject.ui.student.allCourse.adapter.CourseStudentRegisterAdapter
import com.example.baseproject.ui.teacher.allCourse.AllCycleTeacherPopupWindow
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
class AllCourseStudentFragment :
    BaseFragment<FragmentAllCourseStudentBinding, AllCourseStudentViewModel>(R.layout.fragment_all_course_student) {
    private val viewModel: AllCourseStudentViewModel by viewModels()

    override fun getVM(): AllCourseStudentViewModel = viewModel

    private lateinit var adapter: CourseStudentRegisterAdapter

    private val shareViewModel: ShareViewModel by activityViewModels()

    @Inject
    lateinit var rxPreferences: RxPreferences

    @Inject
    lateinit var appNavigation: AppNavigation

    private val dialog = DialogNoticeEmptyImageProfileFragment(onNavigateToScanFace = {
        appNavigation.openToFaceScan()

    })

    private lateinit var allCycleStudentPopupWindow: AllCycleStudentPopupWindow
    private var currentOriginList: List<DetailCourseStudentRegister> = emptyList()


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        viewModel.getDataImageProfile()
        adapter = CourseStudentRegisterAdapter(onCourseClick = {
            val bundle = Bundle()
            bundle.putInt(BundleKey.COURSE_PER_CYCLE_ID, it.coursePerCycleId)
            appNavigation.openStudentTopToDetailScheduleStudent(bundle)
        })
        binding.rvCouse.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvCouse.adapter = adapter

        binding.tvTitle.text = "Hello, " + rxPreferences.getUserName()

        viewModel.getAllCourseRegister()

        allCycleStudentPopupWindow = AllCycleStudentPopupWindow(requireContext()) { cycleClicked ->
            binding.tvAllCourse.text = cycleClicked.cyclesDes
            val item = viewModel.allDetailCourseStudentRegister.value.first { allCycle ->
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

        binding.ivAvatar.loadImage(rxPreferences.getAvatar())

    }

    override fun setOnClick() {
        super.setOnClick()
        binding.ivAvatar.setOnSafeClickListener {
            shareViewModel.setPositionBottomNav(1)
        }

        binding.tvAllCourse.setOnSafeClickListener {
            allCycleStudentPopupWindow.showPopup(binding.tvAllCourse)
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.allDetailCourseStudentRegister.collectFlowOnView(viewLifecycleOwner) {
                allCycleStudentPopupWindow.setData(it)
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

        lifecycleScope.launch {
            viewModel.listDataImageProfile.collectFlowOnView(viewLifecycleOwner) {
                if (it.isEmpty()) {
                    dialog.show(
                        childFragmentManager,
                        DialogNoticeEmptyImageProfileFragment::class.java.simpleName
                    )
                }
            }
        }
    }

}