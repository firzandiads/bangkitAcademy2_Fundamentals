package com.example.submissionawal.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionawal.data.response.listEventsItem
import com.example.submissionawal.databinding.ItemEventsBinding

class eventsAdapter : RecyclerView.Adapter<eventsAdapter.EventViewHolder>() {
    private val events: MutableList<listEventsItem> = mutableListOf()


    inner class EventViewHolder(private val binding: ItemEventsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: listEventsItem) {
            binding.tvName.text = event.name
            Glide.with(binding.root.context).load(event.imageLogo).into(binding.ivThumbnail)
            binding.tvLocation.text = event.cityName
            binding.tvOwner.text = event.ownerName
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }


    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }


    override fun getItemCount(): Int = events.size


    fun setEvents(newEvents: List<listEventsItem?>?) {
        events.clear()
        newEvents?.let {
            it.filterNotNull().let { filteredEvents ->
                events.addAll(filteredEvents)
            }
        }
        notifyDataSetChanged()
    }
}