package com.khaipv.attendance.ui.student.allCourse

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.FragmentAllCourseStudentBinding
import com.khaipv.attendance.model.DetailCourseStudentRegister
import com.khaipv.attendance.navigation.AppNavigation
import com.khaipv.attendance.shareData.ShareViewModel
import com.khaipv.attendance.ui.student.allCourse.adapter.CourseStudentRegisterAdapter
import com.khaipv.attendance.ui.teacher.allCourse.AllCycleTeacherPopupWindow
import com.khaipv.attendance.util.BundleKey
import com.khaipv.attendance.util.DateFormat
import com.khaipv.attendance.util.toDate
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.loadImage
import com.example.core.utils.setOnSafeClickListener
import com.example.core.utils.toastMessage
import com.kbyai.facesdk.FaceBox
import com.kbyai.facesdk.FaceDetectionParam
import com.kbyai.facesdk.FaceSDK
import com.khaipv.attendance.ui.student.faceScan.FaceScanViewModel
import com.khaipv.attendance.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.random.Random

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
        binding.rvCourse.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvCourse.adapter = adapter

        binding.tvTitle.text = "Hello, " + rxPreferences.getName()

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
    private val SELECT_PHOTO_REQUEST_CODE = 1

    override fun setOnClick() {
        super.setOnClick()
        binding.ivAvatar.setOnSafeClickListener {
            shareViewModel.setPositionBottomNav(1)
        }

        binding.tvAllCourse.setOnSafeClickListener {
            allCycleStudentPopupWindow.showPopup(binding.tvAllCourse)
        }
        binding.tvTitle.setOnSafeClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_PICK)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PHOTO_REQUEST_CODE)
        }
    }
    private val faceScanViewModel: FaceScanViewModel by viewModels()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PHOTO_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            try {
                var bitmap: Bitmap = Utils.getCorrectlyOrientedImage(requireContext(), data?.data!!)

                val faceDetectionParam = FaceDetectionParam()
                var faceBoxes: List<FaceBox>? = FaceSDK.faceDetection(bitmap, faceDetectionParam)

                if(faceBoxes.isNullOrEmpty()) {
                    toastMessage("No Face")
                } else if (faceBoxes.size > 1) {
                    toastMessage("Multiple face")
                } else {
                    val templates = FaceSDK.templateExtraction(bitmap, faceBoxes[0])
                    val byteHex = templates.toHex3()
//                    val decodeHex = byteHex.decodeHex()
//                    val isEqual: Boolean = templates.contentEquals(decodeHex)

                    faceScanViewModel.addImageProfileKbyModel(listOf(byteHex))
//                    dbManager.insertPerson("Person" + Random.nextInt(10000, 20000), faceImage, templates)
//                    personAdapter.notifyDataSetChanged()
//                    Toast.makeText(this, getString(R.string.person_enrolled), Toast.LENGTH_SHORT).show()
                }
            } catch (e: java.lang.Exception) {
                //handle exception
                e.printStackTrace()
            }
        }
    }

    fun ByteArray.toHex3(): String = joinToString("") {
        java.lang.Byte.toUnsignedInt(it).toString(radix = 16).padStart(2, '0')
    }

    fun String.decodeHex(): ByteArray {
        check(length % 2 == 0) { "Must have an even length" }

        return chunked(2)
            .map { it.toInt(16).toByte() }
            .toByteArray()
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
                    try {
                        dialog.show(
                            childFragmentManager,
                            DialogNoticeEmptyImageProfileFragment::class.java.simpleName
                        )
                    } catch (_: Exception) {

                    }

                }
            }
        }
    }

}