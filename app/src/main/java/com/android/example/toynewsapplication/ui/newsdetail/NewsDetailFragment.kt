package com.android.example.toynewsapplication.ui.newsdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.android.example.toynewsapplication.databinding.FragmentNewsDetailBinding
import com.bumptech.glide.Glide


class NewsDetailFragment: Fragment() {

    private val args: NewsDetailFragmentArgs by navArgs()
    private val viewModel: NewsDetailViewModel by lazy {
        val viewModelFactory = NewsDetailViewModelFactory(args.news)
        ViewModelProvider(this, viewModelFactory)[NewsDetailViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNewsDetailBinding.inflate(inflater, container, false)

        binding.apply {
            val news = viewModel.selectedNews.value
            news?.let {
                newsTitle.text = it.title
                newsDescription.text = it.content
                newsDate.text = it.publishedAt
                Glide.with(this@NewsDetailFragment)
                    .load(it.urlToImage)
                    .placeholder(android.R.drawable.stat_notify_sync)
                    .error(android.R.drawable.stat_notify_error)
                    .into(newsImage)
            }
        }
        return binding.root
    }

}