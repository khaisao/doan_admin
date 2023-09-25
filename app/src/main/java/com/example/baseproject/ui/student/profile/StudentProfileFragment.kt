package com.example.baseproject.ui.student.profile

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentStudentProfileBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.admin.userProfile.adapter.UserProfileImageViewAdapter
import com.example.core.base.fragment.BaseFragment
import com.example.core.pref.AppPreferences
import com.example.core.pref.RxPreferences
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.setOnSafeClickListener
import com.example.core.utils.toastMessage
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.hbisoft.pickit.PickiT
import com.hbisoft.pickit.PickiTCallbacks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class StudentProfileFragment :
    BaseFragment<FragmentStudentProfileBinding, StudentProfileViewModel>(R.layout.fragment_student_profile),
    PickiTCallbacks {

    @Inject
    lateinit var appNavigation: AppNavigation

    @Inject
    lateinit var rxPreferences: RxPreferences

    private val viewModel: StudentProfileViewModel by viewModels()

    override fun getVM(): StudentProfileViewModel = viewModel

    private lateinit var adapter: UserProfileImageViewAdapter

    private lateinit var detector: FaceDetector

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->

            if (uri != null) {
                val inputImage = InputImage.fromFilePath(requireContext(), uri)
                detector.process(inputImage)
                    .addOnSuccessListener { faces ->
                        if (faces.isNotEmpty()) {
                            if (faces.size > 1) {
                                toastMessage("Please choose a photo with only 1 face")
                            } else {
                                pickiT?.getPath(uri, Build.VERSION.SDK_INT)
                            }
                        } else {
                            toastMessage("Please select a photo that contains a face")
                        }
                    }
                    .addOnFailureListener { e ->
                        toastMessage("Error")
                    }
            } else {

            }
        }

    var pickiT: PickiT? = null


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        pickiT = PickiT(requireContext(), this, requireActivity())
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .build()
        detector = FaceDetection.getClient(options)

        viewModel.getImageProfile()

        Glide.with(requireContext())
            .load(R.drawable.no_avatar)
            .transform(CenterInside(), RoundedCorners(100))
            .into(binding.ivAvatar)
        adapter = UserProfileImageViewAdapter()
        binding.rvImageProfile.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvImageProfile.adapter = adapter
    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.uploadImageActionStateFlow.collectFlowOnView(viewLifecycleOwner) {
                if (it is UploadImageEvent.UploadImageSuccess) {
                    toastMessage("Success")
                }
            }
        }

        lifecycleScope.launch {
            viewModel.listImageProfile.collectFlowOnView(viewLifecycleOwner) {
                Log.d("asgagawgawg", "bindingStateView: $it")
                adapter.submitList(it)
            }
        }
    }

    override fun setOnClick() {
        super.setOnClick()
        binding.tvLogOut.setOnSafeClickListener {
            rxPreferences.remove(AppPreferences.PREF_PARAM_PASSWORD)
            rxPreferences.remove(AppPreferences.PREF_PARAM_USERNAME_LOGIN)
            appNavigation.openLoginScreenAndClearBackStack()
        }

        binding.tvAddImageProfile.setOnSafeClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    override fun PickiTonUriReturned() {

    }

    override fun PickiTonStartListener() {

    }

    override fun PickiTonProgressUpdate(progress: Int) {
    }

    override fun PickiTonCompleteListener(
        path: String?,
        wasDriveFile: Boolean,
        wasUnknownProvider: Boolean,
        wasSuccessful: Boolean,
        Reason: String?
    ) {
        val file = File(path)
        viewModel.updateImageProfile(file)
    }

    override fun PickiTonMultipleCompleteListener(
        paths: ArrayList<String>?,
        wasSuccessful: Boolean,
        Reason: String?
    ) {
    }

}