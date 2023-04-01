package com.android.example.toynewsapplication.ui.newslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.example.toynewsapplication.data.domain.News
import com.android.example.toynewsapplication.databinding.ListItemNewsBinding
import com.bumptech.glide.Glide

class NewsListAdapter(private val onClickListener: OnClickListener) : ListAdapter<News, NewsListViewHolder>(DiffCallback) {

    class OnClickListener(val clickListener: (news: News) -> Unit) {
        fun onClick(news: News) = clickListener(news)
    }

    override fun onBindViewHolder(holder: NewsListViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news, onClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListViewHolder {
        return NewsListViewHolder.from(parent)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }
    }
}

class NewsListViewHolder(private val binding: ListItemNewsBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(news: News, onClickListener: NewsListAdapter.OnClickListener) {
        binding.apply {
            newsTitle.text = news.title
            newsDescription.text = news.description
            newsImage.contentDescription = news.title
            newsDate.text = news.publishedAt
            Glide.with(itemView)
                .load(news.urlToImage)
                .placeholder(android.R.drawable.stat_notify_sync)
                .error(android.R.drawable.stat_notify_error)
                .into(newsImage)
            root.setOnClickListener {
                onClickListener.onClick(news)
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup): NewsListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemNewsBinding.inflate(layoutInflater, parent, false)
            return NewsListViewHolder(binding)
        }
    }
}