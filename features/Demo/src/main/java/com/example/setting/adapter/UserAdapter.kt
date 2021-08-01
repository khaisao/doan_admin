package com.example.setting.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.model.network.ApiUser
import com.example.core.utils.prefetcher.UIJobScheduler
import com.example.core.utils.setTextCompute
import com.example.setting.R
import com.example.setting.databinding.LayoutItemUserBinding
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

const val NUM_CACHED_VIEWS = 14

class UserAdapter constructor(
    context: Context,
) : ListAdapter<ApiUser, UserHolder>(UserDiffUtil()) {

    private val layoutInflater by lazy {
        LayoutInflater.from(context)
    }

    private val asyncLayoutInflater = AsyncLayoutInflater(context)
    private val cachedViews = Stack<LayoutItemUserBinding>()

    init {
        Handler(Looper.getMainLooper()).postDelayed({
            for (i in 0..NUM_CACHED_VIEWS) {
                asyncLayoutInflater.inflate(
                    R.layout.layout_item_user, null
                ) { view, _, _ -> cachedViews.push(DataBindingUtil.bind(view)) }
            }
        }, 0)

    }

    var i = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val binding = if (cachedViews.isEmpty()) {
            Timber.tag("ahihihihi").d("Not cached")
            LayoutItemUserBinding.inflate(layoutInflater, parent, false)
        } else {
            Timber.tag("ahihihihi").d("" + (++i))
            cachedViews.pop().also {
                it.root.layoutParams =
                    ConstraintLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
            }
        }
        return UserHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    override fun submitList(list: List<ApiUser>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).name.hashCode().toLong()
    }

}

class UserHolder(
    val binding: LayoutItemUserBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(data: ApiUser) {

        UIJobScheduler.submitJob { binding.tvName.setTextCompute(data.name) }
        UIJobScheduler.submitJob { binding.tvEmail.setTextCompute(data.email) }

        binding.iv.let {
            Glide.with(it).load("https://www.peterbe.com/avatar.png?seed=$adapterPosition").into(it)
        }
    }

}

class UserDiffUtil : DiffUtil.ItemCallback<ApiUser>() {
    override fun areItemsTheSame(oldItem: ApiUser, newItem: ApiUser): Boolean {
        return oldItem.name == newItem.name && oldItem.id == newItem.id && oldItem.email == newItem.email
    }

    override fun areContentsTheSame(oldItem: ApiUser, newItem: ApiUser): Boolean {
        return oldItem == newItem
    }

}