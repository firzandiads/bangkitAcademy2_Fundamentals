package com.example.submissionawal.vm
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionawal.data.local.model.FavEvents
import com.example.submissionawal.databinding.ItemEventsBinding
import androidx.recyclerview.widget.ListAdapter

class FavoriteAdapter(private val onItemClick: ((Int?) -> Unit)? = null) :
    ListAdapter<FavEvents, FavoriteAdapter.FavoriteEventViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEventViewHolder {
        val binding = ItemEventsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteEventViewHolder(binding, onItemClick)
    }


    override fun onBindViewHolder(holder: FavoriteEventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class FavoriteEventViewHolder(
        private val binding: ItemEventsBinding, private val onItemOnClick: ((Int?) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: FavEvents) {
            binding.tvName.text = event.name
            binding.tvOwner.text = event.ownerName
            binding.tvLocation.text = event.cityName
            Glide.with(binding.ivThumbnail.context)
                .load(event.image)
                .into(binding.ivThumbnail)

            binding.root.setOnClickListener {
                onItemOnClick?.invoke(event.eventId)
            }
        }
    }



    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavEvents>() {
            override fun areItemsTheSame(
                oldItem: FavEvents,
                newItem: FavEvents
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: FavEvents,
                newItem: FavEvents
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}