package com.example.d3grimoire.posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.d3grimoire.R
import com.example.d3grimoire.ImageDecoder

class NewsPostAdapter(
    private val onItemClicked: (NewsData) -> Unit
) : RecyclerView.Adapter<NewsPostAdapter.NewsPostViewHolder>() {

    private var items: List<NewsData> = emptyList()

    fun submitList(data: List<NewsData>) {
        items = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsPostViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_news_post_button, parent, false)
        return NewsPostViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: NewsPostViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class NewsPostViewHolder(
        itemView: View,
        private val onItemClicked: (NewsData) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.news_post_title)
        private val descriptionText: TextView = itemView.findViewById(R.id.news_post_description)
        private val imageView: ImageView = itemView.findViewById(R.id.news_post_image)

        fun bind(data: NewsData) {
            titleText.text = data.name
            descriptionText.text = data.description
            ImageDecoder.trySetImageFromURL(data.imgUrl, imageView)
            itemView.setOnClickListener { onItemClicked(data) }
        }
    }
}
