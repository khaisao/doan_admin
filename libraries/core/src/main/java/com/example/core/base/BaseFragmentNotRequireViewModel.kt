package com.example.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.core.R
import com.example.core.utils.dialog.BaseDialog

abstract class BaseFragmentNotRequireViewModel<BD : ViewDataBinding> : Fragment() {

    private var _binding: BD? = null
    protected val binding: BD get() = _binding!!

    @get: LayoutRes
    abstract val layoutId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        _binding?.lifecycleOwner = viewLifecycleOwner
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            initView(savedInstanceState)

            setOnClick()

            bindingStateView()

            bindingAction()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            initView(savedInstanceState)

            setOnClick()

            bindingStateView()

            bindingAction()
        }
    }

    open fun setOnClick() {

    }

    open fun initView(savedInstanceState: Bundle?) {

    }

    open fun bindingStateView() {

    }

    open fun bindingAction() {

    }

    protected val isDoubleClick: Boolean
        get() {
            if (activity == null) {
                return false
            }
            return if (activity is BaseActivity<*, *>) {
                (activity as BaseActivity<*, *>?)!!.isDoubleClick
            } else false
        }

    override fun onDestroyView() {
        _binding?.unbind()
        _binding = null
        super.onDestroyView()
    }


    fun showHideLoading(isShow: Boolean) {
        if (activity != null && activity is BaseActivity<*, *>) {
            if (isShow) {
                (activity as BaseActivity<*, *>?)!!.showLoading()
            } else {
                (activity as BaseActivity<*, *>?)!!.hiddenLoading()
            }
        }
    }

    fun showAlertDialog(message: String, onPositive: BaseDialog.OnDialogListener? = null) {
        BaseDialog(requireContext())
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(R.string.ok, onPositive)
            .show()
    }

    fun showAlertDialog(@StringRes message: Int) {
        BaseDialog(requireContext())
            .setMessage(message)
            .setPositiveButton(R.string.ok, null)
            .show()
    }

    fun showConfirmDialog(
        @StringRes message: Int,
        onPositive: BaseDialog.OnDialogListener? = null,
        onNegative: BaseDialog.OnDialogListener? = null,
    ) {
        BaseDialog(requireContext())
            .setMessage(message)
            .setPositiveButton(R.string.ok, onPositive)
            .setNegativeButton(R.string.cancel, onNegative)
            .show()
    }

}