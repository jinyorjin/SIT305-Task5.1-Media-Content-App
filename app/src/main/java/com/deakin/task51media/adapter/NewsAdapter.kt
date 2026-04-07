package com.deakin.task51media.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deakin.task51media.R
import com.deakin.task51media.model.NewsItem

class NewsAdapter(
    private var newsList: List<NewsItem>,
    private val onItemClick: (NewsItem) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgNews: ImageView = itemView.findViewById(R.id.imgNews)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtCategory: TextView = itemView.findViewById(R.id.txtCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_news_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = newsList[position]
        holder.imgNews.setImageResource(item.imageResId)
        holder.txtTitle.text = item.title
        holder.txtCategory.text = item.category

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = newsList.size

    // 검색 기능을 위한 리스트 업데이트 함수
    fun updateList(newList: List<NewsItem>) {
        newsList = newList
        notifyDataSetChanged()
    }
}