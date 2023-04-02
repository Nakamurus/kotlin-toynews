package com.android.example.toynewsapplication.ui.newslist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.example.toynewsapplication.data.domain.News
import com.android.example.toynewsapplication.data.local.database.AppDatabase
import com.android.example.toynewsapplication.data.remote.ApiService
import com.android.example.toynewsapplication.data.repository.NewsRepository
import com.android.example.toynewsapplication.databinding.FragmentNewsListBinding
import com.android.example.toynewsapplication.util.ApiState

class NewsListFragment : Fragment() {

    private val viewModel: NewsListViewModel by lazy {
        val viewModelFactory = NewsListViewModelFactory(
            NewsRepository(
                ApiService.newsApiService,
                AppDatabase.getInstance(requireContext()).newsDao()
            )
        )
        ViewModelProvider(this, viewModelFactory)[NewsListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNewsListBinding.inflate(inflater, container, false)
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ApiState.Loading -> showLoadingIndicator(binding)
                is ApiState.Success -> showNewsList(binding)
                is ApiState.Failure -> showErrorText(binding)
            }
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.newsList.layoutManager = layoutManager

        val newsListAdapter = NewsListAdapter(
            NewsListAdapter.OnClickListener { news ->
                navigateToNewsDetail(news)
            })
        binding.newsList.adapter = newsListAdapter

        viewModel.news.observe(viewLifecycleOwner) {
            newsListAdapter.submitList(it)
        }

        return binding.root
    }

    private fun showLoadingIndicator(binding: FragmentNewsListBinding) {
        binding.loadingIndicator.visibility = View.VISIBLE
        binding.errorText.visibility = View.GONE
        binding.newsList.visibility = View.GONE
    }

    private fun showNewsList(binding: FragmentNewsListBinding) {
        binding.loadingIndicator.visibility = View.GONE
        binding.errorText.visibility = View.GONE
        binding.newsList.visibility = View.VISIBLE
    }

    private fun showErrorText(binding: FragmentNewsListBinding) {
        binding.loadingIndicator.visibility = View.GONE
        binding.errorText.visibility = View.VISIBLE
        binding.newsList.visibility = View.GONE
    }

    private fun navigateToNewsDetail(news: News) {
        val action = NewsListFragmentDirections.actionNewsListFragmentToNewsDetailFragment(news)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigateToSelectedNews.observe(viewLifecycleOwner) { news ->
            if (null != news) {
                this.findNavController().navigate(NewsListFragmentDirections.actionNewsListFragmentToNewsDetailFragment(news))
                viewModel.displayNewsDetailsComplete()
            }
        }
    }
}