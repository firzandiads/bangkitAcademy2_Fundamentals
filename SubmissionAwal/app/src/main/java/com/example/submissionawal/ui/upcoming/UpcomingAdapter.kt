package com.example.submissionawal.ui.upcoming
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.submissionawal.ui.rootAdapter
import com.example.submissionawal.data.response.listEventsItem
import com.example.submissionawal.databinding.ItemEventsBinding

class UpcomingAdapter(override val DIFF_CALLBACK: DiffUtil.ItemCallback<listEventsItem>) : rootAdapter<listEventsItem>(DIFF_CALLBACK) {
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



    override fun bind(binding: ItemEventsBinding, item: listEventsItem) {
        binding.tvName.text = item.name
        binding.tvOwner.text = item.ownerName
        binding.tvLocation.text = item.cityName
        Glide.with(binding.root.context).load(item.imageLogo).into(binding.ivThumbnail)


        binding.root.setOnClickListener{
            onItemClickListener?.invoke(item)
        }
    }

}
