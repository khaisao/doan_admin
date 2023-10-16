package com.khaipv.attendance.ui.student.studentProfile

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
import com.khaipv.attendance.R
import com.khaipv.attendance.databinding.FragmentStudentProfileBinding
import com.khaipv.attendance.navigation.AppNavigation
import com.khaipv.attendance.ui.admin.userProfile.adapter.UserProfileImageViewAdapter
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

    private var isChoosingImageProfile = false

    private val pickMediaForProfile =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                isChoosingImageProfile = true
                pickiT?.getPath(uri, Build.VERSION.SDK_INT)

            }
        }

    private var pickiT: PickiT? = null


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        pickiT = PickiT(requireContext(), this, requireActivity())
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .build()
        detector = FaceDetection.getClient(options)

        Glide.with(requireContext())
            .load(rxPreferences.getAvatar())
            .into(binding.ivAvatar)
        adapter = UserProfileImageViewAdapter()
        binding.rvImageProfile.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvImageProfile.adapter = adapter
        binding.tvName.text = rxPreferences.getName()
        binding.tvUsername.text = rxPreferences.getUserName()
    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.uploadAvatarActionStateFlow.collectFlowOnView(viewLifecycleOwner) {
                if (it) {
                    toastMessage("Success")
                    viewModel.getStudentInfo()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.getStudentInfoActionStateFlow.collectFlowOnView(viewLifecycleOwner) {
                if (it) {
                    Glide.with(requireContext())
                        .load(rxPreferences.getAvatar())
                        .into(binding.ivAvatar)
                }
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

        binding.clAddImage.setOnSafeClickListener {
            pickMediaForProfile.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.ivAvatar.setOnSafeClickListener {
            pickMediaForProfile.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.clChangePassword.setOnSafeClickListener {
            appNavigation.openStudentProfileToChangePassword()
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
        viewModel.updateStudentAvatar(file)

    }

    override fun PickiTonMultipleCompleteListener(
        paths: ArrayList<String>?,
        wasSuccessful: Boolean,
        Reason: String?
    ) {
    }

}