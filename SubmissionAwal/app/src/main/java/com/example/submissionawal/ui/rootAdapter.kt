package com.example.submissionawal.ui
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.submissionawal.databinding.ItemEventsBinding

abstract class rootAdapter<T>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, rootAdapter.BaseViewHolder<T>>(diffCallback) {
    var onItemClickListener: ((T) -> Unit)? = null
    abstract val DIFF_CALLBACK: DiffUtil.ItemCallback<T>
    abstract fun bind(binding: ItemEventsBinding, item: T)


    class BaseViewHolder<T>(
        private val binding: ItemEventsBinding,
        private val bind: (ItemEventsBinding, T) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: T, clickListener: ((T) -> Unit)?) {
            bind(binding, item)
            binding.root.setOnClickListener {
                clickListener?.invoke(item)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val binding = ItemEventsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BaseViewHolder(binding, ::bind)
    }


    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bind(getItem(position), onItemClickListener)
    }
}