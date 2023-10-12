package com.example.baseproject.ui.admin.allAcc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentAccTeacherBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.admin.allAcc.adapter.AccStudentAdapter
import com.example.baseproject.ui.admin.allAcc.adapter.AccTeacherAdapter
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.collectFlowOnView
import com.example.core.utils.setOnSafeClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AllAccFragment :
    BaseFragment<FragmentAccTeacherBinding, AllAccViewModel>(R.layout.fragment_acc_teacher) {
    private val viewModel: AllAccViewModel by viewModels()

    override fun getVM(): AllAccViewModel = viewModel

    private lateinit var adapterAccTeacher: AccTeacherAdapter
    private lateinit var adapterAccStudent: AccStudentAdapter

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        adapterAccTeacher = AccTeacherAdapter()
        binding.rvAccTeacher.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvAccTeacher.adapter = adapterAccTeacher

        adapterAccStudent = AccStudentAdapter()
        binding.rvAccStudent.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvAccStudent.adapter = adapterAccStudent

        viewModel.getAllAccount()


    }

    override fun bindingStateView() {
        super.bindingStateView()
        lifecycleScope.launch {
            viewModel.listAccountTeacherResponse.collectFlowOnView(viewLifecycleOwner) {
                adapterAccTeacher.submitList(it.toMutableList())
            }
        }

        lifecycleScope.launch {
            viewModel.listAccountStudentResponse.collectFlowOnView(viewLifecycleOwner) {
                adapterAccStudent.submitList(it)
            }
        }

    }

    override fun setOnClick() {
        super.setOnClick()
        binding.tvTypeAccount.setOnSafeClickListener {
            showPopupChooseTypeAccount()
        }

        binding.ivAddAccount.setOnSafeClickListener {
            appNavigation.openHomeToAddAccount()
        }

        binding.edtSearch.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrBlank()) {
                adapterAccTeacher.submitList(viewModel.listAccountTeacherResponse.value)
                adapterAccStudent.submitList(viewModel.listAccountStudentResponse.value)
            } else {
                adapterAccTeacher.submitList(viewModel.listAccountTeacherResponse.value.filter {
                    it.teacherName.lowercase().contains(text.toString().lowercase())
                })
                adapterAccStudent.submitList(viewModel.listAccountStudentResponse.value.filter {
                    it.studentName.lowercase().contains(text.toString().lowercase())
                })
            }
        }

    }

    private fun showPopupChooseTypeAccount() {
        val inflater = LayoutInflater.from(requireContext())
        val popupView = inflater.inflate(R.layout.pop_up_windown_type_account, null)

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupView.findViewById<TextView>(R.id.tv_all_account).setOnSafeClickListener {
            binding.tvTypeAccount.text = "All account"
            binding.rvAccTeacher.isVisible = true
            binding.rvAccStudent.isVisible = true
            popupWindow.dismiss()
        }

        popupView.findViewById<TextView>(R.id.tv_teacher).setOnSafeClickListener {
            binding.tvTypeAccount.text = getString(R.string.teacher)
            binding.rvAccTeacher.isVisible = true
            binding.rvAccStudent.isVisible = false
            popupWindow.dismiss()
        }

        popupView.findViewById<TextView>(R.id.tv_student).setOnSafeClickListener {
            binding.tvTypeAccount.text = getString(R.string.student)
            binding.rvAccTeacher.isVisible = false
            binding.rvAccStudent.isVisible = true
            popupWindow.dismiss()
        }

        popupWindow.showAsDropDown(binding.tvTypeAccount)
    }

}