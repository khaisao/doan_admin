package com.example.baseproject.ui.teacher.detailCourse

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentDetailCourseBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.teacher.faceReco.FaceRecoFragment
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.setOnSafeClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DetailCourseFragment :
    BaseFragment<FragmentDetailCourseBinding, DetailCourseViewModel>(R.layout.fragment_detail_course) {

    private val viewModel: DetailCourseViewModel by viewModels()

    override fun getVM(): DetailCourseViewModel = viewModel

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        binding.btnAttendance.setOnSafeClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                viewModel.isLoading.postValue(true)
                appNavigation.openDetailCourseToFaceReco()
            }
        }
    }

}