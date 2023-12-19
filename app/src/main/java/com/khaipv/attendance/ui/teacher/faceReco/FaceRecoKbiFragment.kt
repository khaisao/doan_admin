package com.khaipv.attendance.ui.teacher.faceReco

import android.os.Bundle
import android.util.Size
import androidx.fragment.app.viewModels
import com.example.core.base.fragment.BaseFragment
import com.kbyai.facesdk.FaceDetectionParam
import com.kbyai.facesdk.FaceSDK
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.FragmentFaceRecoKbiBinding
import dagger.hilt.android.AndroidEntryPoint
import io.fotoapparat.Fotoapparat
import io.fotoapparat.parameter.Resolution
import io.fotoapparat.preview.Frame
import io.fotoapparat.preview.FrameProcessor
import io.fotoapparat.selector.front
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//adb reverse tcp:3000 tcp:3000
@AndroidEntryPoint
class FaceRecoKbiFragment :
    BaseFragment<FragmentFaceRecoKbiBinding, FaceRecoViewModel>(R.layout.fragment_face_reco_kbi) {

    private val viewModel: FaceRecoViewModel by viewModels()

    override fun getVM(): FaceRecoViewModel = viewModel

    val PREVIEW_WIDTH = 720
    val PREVIEW_HEIGHT = 1280
    private lateinit var fotoapparat: Fotoapparat
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        fotoapparat = Fotoapparat.with(requireContext())
            .into(binding.preview)
            .lensPosition(front())
            .frameProcessor(FaceFrameProcessor())
            .previewResolution { Resolution(PREVIEW_HEIGHT, PREVIEW_WIDTH) }
            .build()

    }

    override fun onResume() {
        super.onResume()
        fotoapparat.start()

    }

    override fun onPause() {
        super.onPause()
        fotoapparat.stop()
        binding.faceView.setFaceBoxes(null, null)
    }

    inner class FaceFrameProcessor : FrameProcessor {

        override fun process(frame: Frame) {

            var cameraMode = 7
//            if (SettingsActivity.getCameraLens(context) == CameraSelector.LENS_FACING_BACK) {
//                cameraMode = 6
//            }

            val bitmap =
                FaceSDK.yuv2Bitmap(frame.image, frame.size.width, frame.size.height, cameraMode)

            val faceDetectionParam = FaceDetectionParam()
            val faceBoxes = FaceSDK.faceDetection(bitmap, faceDetectionParam)

            if (faceBoxes.size > 0) {
                val faceBox = faceBoxes[0]
//                if (faceBox.liveness > SettingsActivity.getLivenessThreshold(context)) {
                val templates = FaceSDK.templateExtraction(bitmap, faceBox)

                var maxSimiarlity = 0f
//                    var maximiarlityPerson: Person? = null
//                    for (person in DBManager.personList) {
//                        val similarity = FaceSDK.similarityCalculation(templates, person.templates)
//                        if (similarity > maxSimiarlity) {
//                            maxSimiarlity = similarity
//                            maximiarlityPerson = person
//                        }
//                    }
//                if (maxSimiarlity > 0.78) {
//                        recognized = true
//                        val identifiedPerson = maximiarlityPerson
                    val identifiedSimilarity = maxSimiarlity
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.faceView.setFrameSize(Size(bitmap.width, bitmap.height))
                        binding.faceView.setFaceBoxes(faceBoxes, listOf("Da detect"))
                    }
//                        runOnUiThread {
//                            val faceImage = Utils.cropFace(bitmap, faceBox)
//                            val intent = Intent(context, ResultActivity::class.java)
//                            intent.putExtra("identified_face", faceImage)
//                            intent.putExtra("enrolled_face", identifiedPerson!!.face)
//                            intent.putExtra("identified_name", identifiedPerson!!.name)
//                            intent.putExtra("similarity", identifiedSimilarity)
//                            intent.putExtra("liveness", faceBox.liveness)
//                            intent.putExtra("yaw", faceBox.yaw)
//                            intent.putExtra("roll", faceBox.roll)
//                            intent.putExtra("pitch", faceBox.pitch)
//                            startActivity(intent)
//                        }
//                }
//                }
            }
        }
    }


}