package com.example.myapplication.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.VenueDetailBinding
import com.example.myapplication.model.Venue

class VenueAdapter(private val items: List<Venue>) :
    RecyclerView.Adapter<VenueAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = VenueDetailBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: VenueDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Venue) {
            binding.locationName.text = item.name
            binding.locationAddress.text = item.address
            binding.locationExtendedAddress.text = item.extendedAddress
            binding.executePendingBindings()
        }
    }
}