package com.example.baseproject.ui.admin.accTeacher.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.databinding.ItemAccountBinding
import com.example.baseproject.model.Account
import com.example.core.utils.setOnSafeClickListener

class AccAdapter(
    val listener: AccClickListener
) :
    ListAdapter<Account, AccAdapter.ConsultantHolder>(DiffCallback()) {

    interface AccClickListener {
        fun onAccClick()
    }

    class DiffCallback : DiffUtil.ItemCallback<Account>() {
        override fun areItemsTheSame(
            oldItem: Account,
            newItem: Account
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Account,
            newItem: Account
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class ConsultantHolder(val binding: ItemAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(course: Account) {
            binding.root.setOnSafeClickListener {
                listener.onAccClick()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultantHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAccountBinding.inflate(inflater, parent, false)
        return ConsultantHolder(binding)
    }

    override fun onBindViewHolder(holder: ConsultantHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
