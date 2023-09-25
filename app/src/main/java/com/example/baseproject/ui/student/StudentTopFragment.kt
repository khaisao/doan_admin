package com.example.baseproject.ui.student

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentStudentTopBinding
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.toastMessage

class StudentTopFragment :
    BaseFragment<FragmentStudentTopBinding, StudentTopViewModel>(R.layout.fragment_student_top) {

    private lateinit var navController: NavController

    private val viewModel: StudentTopViewModel by viewModels()
    override fun getVM(): StudentTopViewModel = viewModel

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setupBottomNavigationBar()
//        binding.bottomNavStudent.setOnItemSelectedListener {
//            when(it.itemId){
//                R.id.scheduleStudentFragment -> {
//                    toastMessage("asgasgasg")
//                }
//                R.id.studentProfileFragment -> {
//
//                }
//
//                else -> {}
//            }
//        }

    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = binding.bottomNavStudent
        navController =
            Navigation.findNavController(activity as Activity, R.id.top_student_nav_host_fragment)
        bottomNavigationView.setupWithNavController(navController)

    }
}