package com.example.setting.ui.login

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.core.base.BaseFragment
import com.example.core.utils.prefetcher.HolderPrefetch
import com.example.core.utils.prefetcher.PrefetchRecycledViewPool
import com.example.setting.DemoNavigation
import com.example.setting.R
import com.example.setting.adapter.SettingAdapter
import com.example.setting.adapter.UserAdapter
import com.example.setting.adapter.UserHolder
import com.example.setting.databinding.FragmentLoginBinding
import com.example.setting.databinding.LayoutItemUserBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(), CoroutineScope {

    @Inject
    lateinit var appNavigation: DemoNavigation

    override val layoutId = R.layout.fragment_login

    private val viewModel: LoginViewModel by viewModels()
    override fun getVM() = viewModel

    private val mAdapter: SettingAdapter by lazy {
        SettingAdapter(
            requireContext(),
            onClickListener = { position, isChecked ->
                viewModel.onChooseCheckbox(position, isChecked)
            }, onClearListener = {
                viewModel.onClear(it)
            })
    }

    private val mUserAdapter: UserAdapter by lazy {
        UserAdapter(requireContext())
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob()

    private val viewPool: PrefetchRecycledViewPool by lazy {
        PrefetchRecycledViewPool(
            requireActivity(),
            this
        ).apply {
            prepare()
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

//        prefetchItems(viewPool)

        binding.listView.setHasFixedSize(true)
        mAdapter.setHasStableIds(true)
        (binding.listView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        binding.listView.adapter = mUserAdapter
//        binding.listView.setRecycledViewPool(viewPool)
    }

    private fun prefetchItems(holderPrefetch: HolderPrefetch) {
        val count = 15
        holderPrefetch.setViewsCount(12, count) { fakeParent, viewType ->
            UserHolder(
                LayoutItemUserBinding.inflate(layoutInflater, fakeParent, false)
            )
        }
    }

    override fun bindingStateView() {

        viewModel.isLoading.observe(viewLifecycleOwner, {
            showHideLoading(it)
        })

        viewModel.listData.observe(viewLifecycleOwner, {
            mAdapter.submitList(it)
        })

        viewModel.listUser.observe(viewLifecycleOwner, {
            mUserAdapter.submitList(it)
        })
    }
}