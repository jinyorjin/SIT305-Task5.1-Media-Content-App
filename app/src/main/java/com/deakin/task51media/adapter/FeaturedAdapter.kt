package com.deakin.task51media.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deakin.task51media.R
import com.deakin.task51media.model.NewsItem

class FeaturedAdapter(
    private val featuredList: List<NewsItem>,
    private val onItemClick: (NewsItem) -> Unit
) : RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder>() {

    class FeaturedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgFeatured: ImageView = itemView.findViewById(R.id.imgFeatured)
        val txtFeaturedTitle: TextView = itemView.findViewById(R.id.txtFeaturedTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_featured_adapter, parent, false)
        return FeaturedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeaturedViewHolder, position: Int) {
        val item = featuredList[position]
        holder.imgFeatured.setImageResource(item.imageResId)
        holder.txtFeaturedTitle.text = item.title

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = featuredList.size
}