package com.practicum.playlistmaker.recycleView


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R

class TrackAdapter(
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {
    var items = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_search_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener { onItemClickListener.onItemClick(items[holder.adapterPosition]) }
    }

}
