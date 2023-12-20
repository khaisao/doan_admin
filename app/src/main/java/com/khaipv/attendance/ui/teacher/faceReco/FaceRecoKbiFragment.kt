package com.khaipv.attendance.ui.teacher.faceReco

import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.setOnSafeClickListener
import com.example.core.utils.toastMessage
import com.kbyai.facesdk.FaceBox
import com.kbyai.facesdk.FaceDetectionParam
import com.kbyai.facesdk.FaceSDK
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.FragmentFaceRecoKbiBinding
import com.khaipv.attendance.navigation.AppNavigation
import com.khaipv.attendance.shareData.ShareViewModel
import com.khaipv.attendance.util.BundleKey
import dagger.hilt.android.AndroidEntryPoint
import io.fotoapparat.Fotoapparat
import io.fotoapparat.parameter.Resolution
import io.fotoapparat.preview.Frame
import io.fotoapparat.preview.FrameProcessor
import io.fotoapparat.selector.front
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

//adb reverse tcp:3000 tcp:3000
@AndroidEntryPoint
class FaceRecoKbiFragment :
    BaseFragment<FragmentFaceRecoKbiBinding, FaceRecoViewModel>(R.layout.fragment_face_reco_kbi) {

    private val viewModel: FaceRecoViewModel by viewModels()

    override fun getVM(): FaceRecoViewModel = viewModel

    private val shareViewModel: ShareViewModel by activityViewModels()

    val PREVIEW_WIDTH = 720
    val PREVIEW_HEIGHT = 1280


    private var courseId: Int? = null
    private var scheduleId: Int? = null

    @Inject
    lateinit var appNavigation: AppNavigation

    private lateinit var fotoapparat: Fotoapparat

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        fotoapparat = Fotoapparat.with(requireContext())
            .into(binding.preview)
            .lensPosition(front())
            .frameProcessor(FaceFrameProcessor())
            .previewResolution { Resolution(PREVIEW_HEIGHT, PREVIEW_WIDTH) }
            .build()
        courseId = arguments?.getInt(BundleKey.COURSE_ID_ATTENDANCE)
        scheduleId = arguments?.getInt(BundleKey.SCHEDULE_ID_ATTENDANCE)
        if (courseId == null || scheduleId == null) {
            toastMessage("Error, try again")
            appNavigation.navigateUp()
        } else {
            viewModel.getAllImageKbiFromCoursePerCycle(courseId!!, 1)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun setOnClick() {
        super.setOnClick()
        binding.tvContinue.setOnSafeClickListener {
            val listStudentRecognized = shareViewModel.listStudentRecognized.value
            shareViewModel.listStudentRecognizedId.addAll(getListIdStudentDetect())
            listStudentRecognized.forEach { item ->
                if (shareViewModel.listStudentRecognizedId.contains(item.studentId)) {
                    item.isReco = true
                }
            }
            val bundle = Bundle()
            bundle.putInt(BundleKey.SCHEDULE_ID_ATTENDANCE, scheduleId!!)
            appNavigation.openFaceRecoKbiToListFaceReco(bundle)
        }
    }

    private lateinit var listUser: List<UserWithByteArray>

    override fun bindingStateView() {
        super.bindingStateView()

        viewModel.imagesKbiData.collectFlowOnView(viewLifecycleOwner) {
            if (it is GetImageKbiUiState.Success) {
                listUser = it.image
                fotoapparat.start()
            }
        }

        viewModel.listStudentRecognized.collectFlowOnView(viewLifecycleOwner) {
            shareViewModel.listStudentRecognized.value = it
        }
    }

    data class FaceDataWithName(
        val face: FaceBox,
        var name: String = "Unknown"
    )

    override fun onPause() {
        super.onPause()
        fotoapparat.stop()
        binding.faceView.setFaceBoxes(null, null)
    }

    val listStudentDetect: MutableSet<String> = mutableSetOf()

    fun getListIdStudentDetect(): List<Int> {
        val set = listStudentDetect.toSet().toList()
        val listId = mutableListOf<Int>()
        for (item in set) {
            val nameArray = item.split("_")
            listId.add(nameArray.last().toInt())
        }
        return listId.toList()
    }

    inner class FaceFrameProcessor : FrameProcessor {
        val listFaceDataWithName = mutableListOf<FaceDataWithName>()
        override fun process(frame: Frame) {

            var cameraMode = 7

            val bitmap =
                FaceSDK.yuv2Bitmap(frame.image, frame.size.width, frame.size.height, cameraMode)

            val faceDetectionParam = FaceDetectionParam()
            val listFaceBoxes = FaceSDK.faceDetection(bitmap, faceDetectionParam)
            listFaceDataWithName.clear()
            for (item in listFaceBoxes) {
                val faceDataWithName = FaceDataWithName(item)
                listFaceDataWithName.add(faceDataWithName)
            }

            if (listFaceDataWithName.size > 0) {
                for (item in listFaceDataWithName) {
                    val faceBox = item.face
                    val templates = FaceSDK.templateExtraction(bitmap, faceBox)

                    var maxSimiarlity = 0f
                    var maximiarlityPerson: UserWithByteArray? = null

                    for (person in listUser) {
                        val similarity = FaceSDK.similarityCalculation(templates, person.byteArray)

                        if (similarity > maxSimiarlity) {
                            maxSimiarlity = similarity
                            maximiarlityPerson = person
                        }
                        if (maxSimiarlity > 0.78) {
                            val identifiedPerson = maximiarlityPerson
                            if (identifiedPerson != null) {
                                item.name = identifiedPerson.name
                                listStudentDetect.add(identifiedPerson.name)
                            }
                        }
                    }

                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            binding.faceView.setFrameSize(Size(bitmap.width, bitmap.height))
                            binding.faceView.setFaceBoxes(
                                listFaceDataWithName
                            )
                        } catch (e: Exception) {

                        }
                    }
                }
            }
        }
    }
}