package com.example.submissionawal.vm
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.example.submissionawal.data.remote.response.listEventsItem
import com.example.submissionawal.databinding.ItemEventsBinding


class FinishAdapter(private val onItemClick: ((Int?) -> Unit)? = null) :
    ListAdapter<listEventsItem, FinishAdapter.FinishedEventViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinishedEventViewHolder {
        val binding = ItemEventsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FinishedEventViewHolder(binding, onItemClick)
    }


    override fun onBindViewHolder(holder: FinishedEventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class FinishedEventViewHolder(
        private val binding: ItemEventsBinding, private val onItemClick: ((Int?) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: listEventsItem) {
            binding.tvName.text = event.name
            binding.tvOwner.text = event.ownerName
            binding.tvLocation.text = event.cityName
            Glide.with(binding.root.context).load(event.imageLogo).into(binding.ivThumbnail)

            binding.root.setOnClickListener {
                onItemClick?.invoke(event.id)
            }
        }
    }



    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<listEventsItem>() {
            override fun areItemsTheSame(
                oldItem: listEventsItem,
                newItem: listEventsItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: listEventsItem,
                newItem: listEventsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}