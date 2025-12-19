package com.example.submissionawal.vm
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.example.submissionawal.data.remote.response.listEventsItem
import com.example.submissionawal.databinding.ItemEventsBinding

class UpcomingAdapter(private val onItemClick: ((Int?) -> Unit)? = null) :
    ListAdapter<listEventsItem, UpcomingAdapter.UpcomingEventViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingEventViewHolder {
        val binding = ItemEventsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UpcomingEventViewHolder(binding, onItemClick)
    }


    override fun onBindViewHolder(holder: UpcomingEventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }



    class UpcomingEventViewHolder(
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

