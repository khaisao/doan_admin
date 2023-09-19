package com.example.baseproject.ui.admin.accTeacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentAccTeacherBinding
import com.example.baseproject.model.Account
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.admin.accTeacher.adapter.AccAdapter
import com.example.core.base.fragment.BaseFragment
import com.example.core.utils.setOnSafeClickListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccTeacherFragment :
    BaseFragment<FragmentAccTeacherBinding, AccTeacherViewModel>(R.layout.fragment_acc_teacher),
    AccAdapter.AccClickListener {
    private val viewModel: AccTeacherViewModel by viewModels()

    override fun getVM(): AccTeacherViewModel = viewModel

    private lateinit var adapter: AccAdapter

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        adapter = AccAdapter(this)
        binding.rvAcc.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvAcc.adapter = adapter
        val listAcc = listOf(
            Account(
                id = 3610,
                email = "hal.roth@example.com",
                password = "mus",
                name = "Robbie Stanton",
                role = 9472
            ), Account(
                id = 3610,
                email = "hal.roth@example.com",
                password = "mus",
                name = "Robbie Stanton",
                role = 9472
            ), Account(
                id = 3610,
                email = "hal.roth@example.com",
                password = "mus",
                name = "Robbie Stanton",
                role = 9472
            ), Account(
                id = 3610,
                email = "hal.roth@example.com",
                password = "mus",
                name = "Robbie Stanton",
                role = 9472
            ), Account(
                id = 3610,
                email = "hal.roth@example.com",
                password = "mus",
                name = "Robbie Stanton",
                role = 9472
            )
        )
        adapter.submitList(listAcc)

    }

    override fun setOnClick() {
        super.setOnClick()
        binding.tvTypeAccount.setOnSafeClickListener {
            showPopupChooseTypeAccount()
        }

        binding.ivAddAccount.setOnSafeClickListener {
            appNavigation.openHomeToAddAccount()
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

        popupView.findViewById<TextView>(R.id.tv_teacher).setOnSafeClickListener {
            binding.tvTypeAccount.text = getString(R.string.teacher)
            popupWindow.dismiss()
        }

        popupView.findViewById<TextView>(R.id.tv_student).setOnSafeClickListener {
            binding.tvTypeAccount.text = getString(R.string.student)
            popupWindow.dismiss()
        }

        popupWindow.showAsDropDown(binding.tvTypeAccount)
    }

    override fun onAccClick() {
        appNavigation.openHomeToUserProfile()
    }

}