package com.khaipv.attendance.ui.student.allCourse

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.loadImage
import com.example.core.utils.setOnSafeClickListener
import com.example.core.utils.toastMessage
import com.kbyai.facesdk.FaceBox
import com.kbyai.facesdk.FaceDetectionParam
import com.kbyai.facesdk.FaceSDK
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.FragmentAllCourseStudentBinding
import com.khaipv.attendance.model.DetailCourseStudentRegister
import com.khaipv.attendance.navigation.AppNavigation
import com.khaipv.attendance.shareData.ShareViewModel
import com.khaipv.attendance.ui.student.allCourse.adapter.CourseStudentRegisterAdapter
import com.khaipv.attendance.ui.student.faceScan.FaceScanViewModel
import com.khaipv.attendance.util.BundleKey
import com.khaipv.attendance.util.DateFormat
import com.khaipv.attendance.util.Utils
import com.khaipv.attendance.util.toDate
import com.khaipv.attendance.util.toHex3
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
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

        binding.ivAvatar.loadImage(rxPreferences.getAvatar(), R.drawable.no_avatar)
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
//            val screenshotDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/Screenshots"
//            val file = File(screenshotDir)
//            for(item in file.listFiles()!!){
//                toastMessage(item.absolutePath)
//                getAllImagesFromFolder(item.absolutePath)
//            }
        }
    }

    private val faceScanViewModel: FaceScanViewModel by viewModels()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PHOTO_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            try {
                var bitmap: Bitmap = Utils.getCorrectlyOrientedImage(requireContext(), data?.data!!)

                val faceDetectionParam = FaceDetectionParam()
                val faceBoxes: List<FaceBox>? = FaceSDK.faceDetection(bitmap, faceDetectionParam)

                if (faceBoxes.isNullOrEmpty()) {
                    toastMessage("No Face")
                } else if (faceBoxes.size > 1) {
                    toastMessage("Multiple face")
                } else {
                    val templates = FaceSDK.templateExtraction(bitmap, faceBoxes[0])
                    val byteHex = templates.toHex3()
                    faceScanViewModel.addImageProfileKbyModel(listOf(byteHex))
                }
            } catch (e: java.lang.Exception) {
                //handle exception
                e.printStackTrace()
            }
        }
    }

    fun getAllImagesFromFolder(filePath: String) {
        val folder = File(filePath)
        Log.d("asgagwawgawg", "getAllImagesFromFolder: $filePath")
        Log.d("asgagwawgawg", "exists: ${folder.exists()}")
        Log.d("asgagwawgawg", "getAllImagesFromFolder: ${folder.isDirectory()}")
        if (folder.exists() && folder.isDirectory) {
            val files = folder.listFiles()
            val listFile = mutableListOf<File>()
            for (file in files) {
                Log.d("asgagwawgawg", "getAllImagesFromFolder: ${file.extension}")
                if (file.isFile && file.extension == "jpg" || file.extension == "png") {
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)

//                    var bitmap: Bitmap = Utils.getCorrectlyOrientedImage(requireContext(), Uri.fromFile(file))

                    val faceDetectionParam = FaceDetectionParam()
                    var faceBoxes: List<FaceBox>? =
                        FaceSDK.faceDetection(bitmap, faceDetectionParam)

                    if (faceBoxes.isNullOrEmpty()) {
                        toastMessage("No Face")
                    } else if (faceBoxes.size > 1) {
                        toastMessage("Multiple face")
                    } else {
                        val templates = FaceSDK.templateExtraction(bitmap, faceBoxes[0])
                        val byteHex = templates.toHex3()
                        faceScanViewModel.addImageProfileKbyModel(listOf(byteHex))
                    }
                    listFile.add(file)
                }
            }
            toastMessage(listFile.size.toString())
        }
    }


    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.allDetailCourseStudentRegister.collectFlowOnView(viewLifecycleOwner) {
                if(it.isNotEmpty()){
                    allCycleStudentPopupWindow.setData(it)
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
                } else {
                    toastMessage("Student don't have any course")
                }
            }
        }

        lifecycleScope.launch {
            viewModel.listDataImageProfileUiState.collectFlowOnView(viewLifecycleOwner) { uiState ->
                if (uiState is GetDataImageProfileUiState.Success) {
                    try {
                        if (uiState.listDataImageProfile.isEmpty()) {
                            dialog.show(
                                childFragmentManager,
                                DialogNoticeEmptyImageProfileFragment::class.java.simpleName
                            )
                        }
                    } catch (_: Exception) {
                    }
                }
            }
        }
    }

}